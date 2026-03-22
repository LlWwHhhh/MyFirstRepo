package com.repair.service;

import com.repair.mapper.UserMapper;
import com.repair.pojo.User;
import com.repair.utils.MyBatisUtil;
import com.repair.utils.RegexUtil;
import org.apache.ibatis.session.SqlSession;

public class UserService {
    /**
     * 用户注册方法
     * @param role 角色（1-学生，2-维修人员）
     * @param name 名字
     * @param account 学号/工号
     * @param password 密码
     * @param confirmPwd 确认密码
     * @return 注册结果
     */
    public String register(Integer role, String account, String password, String username, String dormBuilding, String dormRoom, String confirmPwd) {
        if (account == null || account.isEmpty()) return "❌ 账号不能为空！";
        if (password == null || password.isEmpty()) return "❌ 密码不能为空！";
        if (username == null || username.isEmpty()) return "❌ 请输入姓名！";
        if (!password.equals(confirmPwd)) return "❌ 两次密码不一致！";

        if (role == 1) {
            if (!RegexUtil.isStudentAccount(account)) {
                return "❌ 学生学号格式错误！";
            }
            if (dormBuilding == null || dormBuilding.isEmpty()) return "❌ 请输入楼栋！";
            if (dormRoom == null || dormRoom.isEmpty()) return "❌ 请输入房间号！";
        }else{
            if (!RegexUtil.isAdminAccount(account)) {
                return "❌ 教职工号格式错误！";
            }
        }

        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            User exist = mapper.selectUserByAccount(account);
            if (exist != null) return "❌ 账号已存在！";

            User user = new User();
            user.setAccount(account);
            user.setPassword(password);
            user.setRole(role);
            user.setUsername(username);

            // 【只有学生才赋值这三个】
            if (role == 1) {
                user.setDormBuilding(dormBuilding);
                user.setDormRoom(dormRoom);
            }

            mapper.insertUser(user);
            return "✅ 注册成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ 注册异常";
        }
    }

    /**
     * 用户登录方法
     * @param role 登录角色（1-学生，2-教职工）
     * @param account 学号/工号
     * @param password 密码
     * @return 登录结果
     */
    public String login(Integer role, String account, String password) {
        // 1. 非空校验
        if (account == null || account.length() == 0) {
            return "❌ 账号不能为空！";
        }
        if (password == null || password.length() == 0) {
            return "❌ 密码不能为空！";
        }

        // 2. 账号格式校验
        if (role == 1) {
            if (!RegexUtil.isStudentAccount(account)) {
                return "❌ 学生学号格式错误！需以3125/3225开头，后跟6位数字（如3125004123）";
            }
        } else if (role == 2) {
            if (!RegexUtil.isAdminAccount(account)) {
                return "❌ 教职工工号格式错误！需以0025开头，后跟6位数字（如0025004123）";
            }
        } else {
            return "❌ 角色选择错误！只能选1（学生）或2（教职工）";
        }

        // 3. 数据库校验
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            User loginUser = userMapper.selectUserByLogin(account, password, role);

            if (loginUser == null) {
                return "❌ 账号/密码/角色不匹配！请检查后重新输入";
            } else {
                return "✅ 登录成功！欢迎你，" + (role == 1 ? "学生" : "教职工") + "：" + loginUser.getUsername();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ 登录异常：" + e.getMessage();
        }
    }

    // 修改密码
    public String updatePwd(String account, String oldPwd, String newPwd) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            User user = mapper.selectUserByAccount(account);
            if (!user.getPassword().equals(oldPwd)) {
                return "❌ 原密码错误";
            }
            mapper.updatePwd(account, newPwd);
            return "✅ 密码修改成功";
        }
    }

    // 1. 根据账号查询整个用户信息（用来拿宿舍）
    public User getUserByAccount(String account) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.selectUserByAccount(account);
        }
    }

    // 2. 修改学生绑定的宿舍（学生菜单1要用）
    public String updateDorm(String account, String newBuilding, String newRoom) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            int rows = mapper.updateDorm(account, newBuilding, newRoom);
            session.commit();
            return rows > 0 ? "✅ 宿舍修改成功！" : "❌ 宿舍修改失败！";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ 修改宿舍异常";
        }
    }
}
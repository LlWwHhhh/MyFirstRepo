package com.example.demo_repaire_system.service.impl;

import com.example.demo_repaire_system.entity.Result;
import com.example.demo_repaire_system.entity.User;
import com.example.demo_repaire_system.mapper.UserMapper;
import com.example.demo_repaire_system.service.UserService;
import com.example.demo_repaire_system.util.JwtUtil;
import com.example.demo_repaire_system.util.RegexUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册方法
     * @param role 角色（1-学生，2-维修人员）
     * @param username 名字
     * @param account 学号/工号
     * @param password 密码
     * @param confirmPwd 确认密码
     * @return 注册结果
     */
    @Transactional
    @Override
    public String register(Integer role, String account, String password, String username, String dormBuilding, String dormRoom, String confirmPwd) {
        if (account == null || account.isEmpty()) return "❌ 账号不能为空！";
        if (password == null || password.isEmpty()) return "❌ 密码不能为空！";
        if (username == null || username.isEmpty()) return "❌ 请输入姓名！";
        if (!password.equals(confirmPwd)) return "❌ 两次密码不一致！";

        UserService userService;

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

        User existUser = userMapper.selectUserByAccount(account);
        if(existUser != null)return "账号已存在！";

        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setRole(role);
        user.setUsername(username);

        if (role == 1) {
            user.setDormBuilding(dormBuilding);
            user.setDormRoom(dormRoom);
        }

        userMapper.insertUser(user);
        return "注册成功！";
    }

    /**
     * 用户登录方法
     * @param role 登录角色（1-学生，2-教职工）
     * @param account 学号/工号
     * @param password 密码
     * @return 登录结果
     */
    @Override
    public Result login(Integer role, String account, String password) {
        Result result = new Result();

        // 1. 校验账号密码
        User user = selectUserByAccount(account);

        // 2. 校验逻辑：用户存在 + 密码正确 + 角色匹配
        if (user != null && user.getPassword().equals(password) && user.getRole().equals(role)) {
            // 3. 【核心】生成JWT Token
            String token = JwtUtil.generateToken(account);

            // 4. 给Result赋值
            result.setSuccess(true);
            result.setMsg("登录成功");
            result.setUser(user);
            result.setToken(token); // ✅ 把Token存入Result
        } else {
            // 5. 登录失败
            result.setSuccess(false);
            result.setMsg("账号、密码或角色错误");
            result.setUser(null);
            result.setToken(null);
        }

        return result;
    }

    // 修改密码
    @Transactional
    @Override
    public Result updatePwd(String account, String oldPwd, String newPwd) {
        User user = userMapper.selectUserByAccount(account);
        if(user == null)return Result.fail("账号不存在！");
        if(!user.getPassword().equals(oldPwd)){
            return Result.fail("原密码错误");
        }
        userMapper.updatePwd(account,newPwd);
        return Result.success("密码修改成功!");
    }

    // 1. 根据账号查询整个用户信息（用来拿宿舍）
    @Override
    public User getUserByAccount(String account) {
        return userMapper.selectUserByAccount(account);
    }

    // 2. 修改学生绑定的宿舍（学生菜单1要用）
    @Transactional
    @Override
    public Result updateDorm(String account, String newBuilding, String newRoom) {
        if (newBuilding == null || newRoom == null || newBuilding.isEmpty() || newRoom.isEmpty()) {
            return Result.fail("❌ 楼栋和房间不能为空！");
        }

        int rows = userMapper.updateDorm(account, newBuilding, newRoom);
        return rows > 0 ? Result.success("✅ 宿舍修改成功！") : Result.fail("❌ 宿舍修改失败！");
    }

    @Override
    public User selectUserByAccount(String account) {
        return userMapper.selectUserByAccount(account);
    }
}

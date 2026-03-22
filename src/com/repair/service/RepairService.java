package com.repair.service;

import com.repair.mapper.RepairOrderMapper;
import com.repair.pojo.RepairOrder;
import com.repair.pojo.User;
import com.repair.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Scanner;

public class RepairService {
    // 学生提交报修
    public String addRepairOrder(String studentAccount, String problem) {

        if (problem == null || problem.length() == 0) {
            return "❌ 问题描述不能为空！";
        }

        // 调用UserService查询学生绑定的宿舍
        UserService userService = new UserService();
        User student = userService.getUserByAccount(studentAccount);
        if (student == null || student.getDormBuilding() == null || student.getDormRoom() == null) {
            return "❌ 请先绑定宿舍！";
        }

        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            RepairOrder order = new RepairOrder();
            order.setStudentAccount(studentAccount);
            order.setDormBuilding(student.getDormBuilding()); // 自动填充楼栋
            order.setDormRoom(student.getDormRoom()); // 自动填充房间
            order.setProblem(problem);
            order.setStatus("待处理");
            int rows = mapper.addRepairOrder(order);
            session.commit();
            return rows > 0 ? "✅ 报修成功！等待处理" : "❌ 报修失败";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ 报修异常：" + e.getMessage();
        }
    }

    // 学生查看自己的报修单
    public List<RepairOrder> getMyOrders(String account) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            return mapper.getOrdersByStudent(account);
        }
    }

    // 教职工查看所有报修单
    public List<RepairOrder> getAllOrders() {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            return mapper.getAllOrders();
        }
    }

    // 学生删除报修
    public String deleteRepairOrder(Integer id, String account) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            int rows = mapper.deleteByStudent(id, account);
            return rows > 0 ? "✅ 删除成功" : "❌ 删除失败！只能删除自己创建的未处理的订单！";
        }
    }

    // 学生删除报修单
    public String deleteByStudent(Integer id, String account) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            String status = mapper.getStatusById(id);

            if (status == null) {
                return "❌ 订单不存在！";
            }
            if ("已处理".equals(status)) {
                return "❌ 已处理的订单不能删除！";
            }

            int rows = mapper.deleteByStudent(id, account);
            session.commit();
            return rows > 0 ? "✅ 订单已取消" : "❌ 只能取消自己的待处理订单";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ 删除异常：" + e.getMessage();
        }
    }

    // 管理员删除报修单（带确认）
    public String deleteByAdmin(Integer id, boolean confirm) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            String status = mapper.getStatusById(id);

            if (status == null) {
                return "❌ 订单不存在！";
            }
            if ("待处理".equals(status) && !confirm) {
                System.out.println("⚠️ 该订单未处理，确认删除请输入 'yes'");
                Scanner sc=new Scanner(System.in);
                String result=sc.nextLine();
                if(!result.equals("yes")){
                    return "已删除取消！";
                }
            }

            int rows = mapper.deleteByAdmin(id);
            session.commit();
            return rows > 0 ? "✅ 订单已删除" : "❌ 删除失败";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ 删除异常：" + e.getMessage();
        }
    }

    // 管理员改状态
    public String updateStatus(Integer id, String status) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            mapper.updateOrderStatus(id, status);
            session.commit(); // 必须提交事务！
            return "✅ 状态已更新";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ 更新异常：" + e.getMessage();
        }
    }

}

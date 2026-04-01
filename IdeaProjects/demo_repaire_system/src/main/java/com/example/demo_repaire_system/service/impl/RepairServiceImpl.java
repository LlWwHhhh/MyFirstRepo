package com.example.demo_repaire_system.service.impl;

import com.example.demo_repaire_system.entity.RepairOrder;
import com.example.demo_repaire_system.entity.Result;
import com.example.demo_repaire_system.entity.User;
import com.example.demo_repaire_system.mapper.RepairOrderMapper;
import com.example.demo_repaire_system.service.RepairService;
import com.example.demo_repaire_system.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairServiceImpl implements RepairService {
    @Autowired//Spring自动注入Mapper，不用自己new
    private RepairOrderMapper repairOrderMapper;//报修单Mapper

    @Autowired
    private UserService userService;//注入用户Service,不用手动new

    // 学生提交报修
    @Transactional
    @Override
    public Result addRepairOrder(String studentAccount, String problem, String imagePath, String dormBuilding, String dormRoom) {
        if (problem == null || problem.isEmpty()) {
            return Result.fail("❌ 问题描述不能为空！");
        }

        User student = userService.selectUserByAccount(studentAccount);
        if (student == null) {
            return Result.fail("❌ 学生账号不存在！");
        }
        if (student.getDormBuilding() == null || student.getDormRoom() == null) {
            return Result.fail("❌ 请先绑定宿舍！当前数据库值：" + student.getDormBuilding() + "/" + student.getDormRoom());
        }

        RepairOrder order = new RepairOrder();
        order.setStudentAccount(studentAccount);
        order.setDormBuilding(student.getDormBuilding());
        order.setDormRoom(student.getDormRoom());
        order.setProblem(problem);
        order.setImagePath(imagePath);
        order.setStatus("待处理");

        int rows = repairOrderMapper.addRepairOrder(order);
        return rows > 0 ? Result.success("✅ 报修成功！等待处理") : Result.fail("❌ 报修失败");
    }

    // 学生查看自己的报修单
    @Override
    public List<RepairOrder> getMyOrders(String account) {
        return repairOrderMapper.getOrdersByStudent(account);
    }

    // 教职工查看1.所有报修单
    @Override
    public List<RepairOrder> getAllOrders() {
        return repairOrderMapper.getAllOrders();
    }

    //2.按状态查找
    @Override
    public List<RepairOrder> getOrdersByStatus(String status) {
        return repairOrderMapper.selectOrdersByStatus(status);
    }



    // 学生删除报修单
    @Transactional//增删改要加事务
    @Override
    public Result deleteByStudent(Integer id, String account) {
        String status = repairOrderMapper.getStatusById(id);
        if(status==null){
            return Result.fail("订单不存在！");
        }
        if("已处理".equals(status)){
            return Result.fail("已处理的订单无法删除！");
        }
        int rows = repairOrderMapper.deleteByStudent(id, account);
        return rows > 0 ? Result.success("订单已成功取消！") : Result.fail("只能取消自己的待处理的订单！");
    }

    // 管理员删除报修单（带确认）
    @Transactional
    @Override
    public Result deleteByAdmin(Integer id, boolean confirm) {
        String status =repairOrderMapper.getStatusById(id);

        if(status==null){
            return Result.fail("订单不存在！");
        }
        if("待处理".equals(status) && !confirm){
            return Result.fail("该订单未处理，确认删除请传入confirm=true！");
        }

        int rows = repairOrderMapper.deleteByAdmin(id);
        return rows > 0 ? Result.success("订单已删除") : Result.fail("删除失败");
    }

    // 管理员改状态
    @Transactional
    @Override
    public Result updateStatus(Integer id, String status) {
        int rows = repairOrderMapper.updateOrderStatus(id, status);
        return rows > 0 ? Result.success("✅ 状态更新成功！") : Result.fail("❌ 状态更新失败！");
    }
}

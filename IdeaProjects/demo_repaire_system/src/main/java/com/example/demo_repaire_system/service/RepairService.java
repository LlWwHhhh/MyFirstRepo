package com.example.demo_repaire_system.service;

import com.example.demo_repaire_system.entity.RepairOrder;
import com.example.demo_repaire_system.entity.Result;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RepairService {
    Result addRepairOrder(String studentAccount, String problem,String imagePath,String dormBuilding,String dormRoom);//学生提交报修单
    List<RepairOrder> getMyOrders(String account);//学生查看自己的报修单
    List<RepairOrder> getAllOrders();//教职工：1.查看所有报修单
    List<RepairOrder> getOrdersByStatus(String status);//2.按状态查询报修单
    Result deleteByStudent(Integer id, String account);// 学生删除报修
    Result deleteByAdmin(Integer id, boolean confirm);// 管理员删除报修单（带确认）
    Result updateStatus(Integer id, String status);// 管理员改状态
}

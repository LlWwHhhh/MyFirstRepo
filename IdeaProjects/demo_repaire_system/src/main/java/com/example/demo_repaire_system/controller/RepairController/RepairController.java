package com.example.demo_repaire_system.controller.RepairController;

import com.example.demo_repaire_system.entity.RepairOrder;
import com.example.demo_repaire_system.entity.Result;
import com.example.demo_repaire_system.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair")
public class RepairController {
    @Autowired
    private RepairService repairService;

    //学生：新建报修单
    @GetMapping("/add")
    public Result addRepair(
            @RequestParam String studentAccount,
            @RequestParam String problem,
            @RequestParam(required = false) String imagePath,
            @RequestParam String dormBuilding,
            @RequestParam String dormRoom
    ){
        return repairService.addRepairOrder(studentAccount, problem, imagePath, dormBuilding, dormRoom);
    }

    //学生：查看我的报修单
    @GetMapping("/my-order")
    public List<RepairOrder> getMyOrders(@RequestParam String account){
        return repairService.getMyOrders(account);
    }

    //教职工：查看所有报修单
    @GetMapping("/all")
    public List<RepairOrder> getAllOrders() {
        return repairService.getAllOrders();
    }

    // 2. 查询待处理报修单（状态为 0）
    @GetMapping("/pending")
    public List<RepairOrder> getPendingOrders() {
        return repairService.getOrdersByStatus("待处理");
    }

    // 3. 查询已完成报修单（状态为 1）
    @GetMapping("/completed")
    public List<RepairOrder> getCompletedOrders() {
        return repairService.getOrdersByStatus("已处理");
    }

    //学生：删除报修单
    @GetMapping("/deleteByStudent")
    public Result deleteByStudent(
           @RequestParam Integer id,
           @RequestParam String account
    ){
        return repairService.deleteByStudent(id, account);
    }

    //教职工：删除报修单
    @GetMapping("/deleteByAdmin")
    public Result deleteByAdmin(
            @RequestParam Integer id,
            @RequestParam(required = false) boolean confirm
    ) {
        return repairService.deleteByAdmin(id, confirm);
    }

    //教职工：更新保修单的状态
    @GetMapping("/updateStatus")
    public Result updateStatus(
            @RequestParam Integer id,
            @RequestParam String status
    ) {
        return repairService.updateStatus(id, status);
    }

}

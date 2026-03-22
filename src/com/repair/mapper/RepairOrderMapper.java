package com.repair.mapper;

import com.repair.pojo.RepairOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RepairOrderMapper {
    // 学生提交报修
    @Insert("INSERT INTO repair_order(student_account, dorm_building, dorm_room, problem, status) " +
            "VALUES(#{studentAccount}, #{dormBuilding}, #{dormRoom}, #{problem}, #{status})")
    int addRepairOrder(RepairOrder order);

    // 学生查看自己的报修单
    @Select("SELECT * FROM repair_order WHERE student_account = #{studentAccount}")
    List<RepairOrder> getOrdersByStudent(@Param("studentAccount") String account);

    // 教职工查看所有报修单
    @Select("SELECT * FROM repair_order")
    List<RepairOrder> getAllOrders();

    // 学生删除：只能删自己的订单
    @Delete("DELETE FROM repair_order WHERE id=#{id} AND student_account=#{account}")
    int deleteByStudent(@Param("id") Integer id, @Param("account") String account);

    // 管理员删除：可删任意订单
    @Delete("DELETE FROM repair_order WHERE id=#{id}")
    int deleteByAdmin(@Param("id") Integer id);

    // 查询订单状态，用于删除前确认
    @Select("SELECT status FROM repair_order WHERE id=#{id}")
    String getStatusById(@Param("id") Integer id);

    // 管理员修改订单状态 ✅【修复重点】
    @Update("UPDATE repair_order SET status=#{status} WHERE id=#{id}")
    int updateOrderStatus(@Param("id") Integer id, @Param("status") String status);
}

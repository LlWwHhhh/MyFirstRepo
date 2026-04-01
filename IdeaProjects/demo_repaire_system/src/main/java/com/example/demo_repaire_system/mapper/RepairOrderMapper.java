package com.example.demo_repaire_system.mapper;

import com.example.demo_repaire_system.entity.RepairOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RepairOrderMapper {
    @Insert("INSERT INTO repair_order (student_account, dorm_building, dorm_room, problem, image_path, status) " +
               "VALUES(#{studentAccount}, #{dormBuilding}, #{dormRoom}, #{problem}, #{imagePath}, #{status})")
    int addRepairOrder(RepairOrder order);

    // 教职工按照状态查看订单
    @Select("SELECT id, student_account AS studentAccount, dorm_building AS dormBuilding, " +
            "dorm_room AS dormRoom, problem, status, image_path AS imagePath, create_time AS createTime " +
            "FROM repair_order WHERE status = #{status}")
    List<RepairOrder> selectOrdersByStatus(String status);

    // 学生查看自己的报修单
    @Select("SELECT id, student_account AS studentAccount, dorm_building AS dormBuilding, " +
             "dorm_room AS dormRoom, problem, status, image_path AS imagePath " +
            "FROM repair_order WHERE student_account = #{studentAccount}")
    List<RepairOrder> getOrdersByStudent(@Param("studentAccount") String account);


    @Select("SELECT id, student_account AS studentAccount, dorm_building AS dormBuilding, " +
            "dorm_room AS dormRoom, problem, status, image_path AS imagePath, create_time AS createTime " +
            "FROM repair_order")
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

    // 管理员修改订单状态
    @Update("UPDATE repair_order SET status=#{status} WHERE id=#{id}")
    int updateOrderStatus(@Param("id") Integer id, @Param("status") String status);

}

package com.example.demo_repaire_system.mapper;

import com.example.demo_repaire_system.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    // 根据账号查询用户（校验账号是否存在+添加报修单）
    @Select("SELECT id, account, password, role, username, " +
            "dorm_building AS dormBuilding, " +  // 强制别名
            "dorm_room AS dormRoom " +
            "FROM user WHERE account = #{account}")
    User selectUserByAccount(String account);

    // 注册：插入用户信息
    @Insert("INSERT INTO user (account,password,role,username,dorm_building,dorm_room) " +
            "VALUES(#{account},#{password},#{role},#{username},#{dormBuilding},#{dormRoom})")
    int insertUser(User user);

    // 登录
    @Select("SELECT * FROM user WHERE account = #{account} AND password = #{password} AND role = #{role}")
    User selectUserByLogin(@Param("account") String account,
                           @Param("password") String password,
                           @Param("role") Integer role);

    // 修改密码
    @Update("UPDATE user SET password=#{pwd} WHERE account=#{account}")
    int updatePwd(@Param("account") String account, @Param("pwd") String pwd);

    // 根据账号更新宿舍
    @Update("UPDATE user SET dorm_building=#{dormBuilding}, dorm_room=#{dormRoom} WHERE account=#{account}")
    int updateDorm(
            @Param("account") String account,
            @Param("dormBuilding") String dormBuilding,
            @Param("dormRoom") String dormRoom
    );
}
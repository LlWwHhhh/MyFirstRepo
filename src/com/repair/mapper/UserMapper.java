package com.repair.mapper;

import com.repair.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    // 注册：根据账号查询用户（校验账号是否存在）
    @Select("SELECT * FROM user WHERE account = #{account}")
    User selectUserByAccount(String account);

    @Insert("INSERT INTO user (account,password,role,username,dorm_building,dorm_room) " +
            "VALUES(#{account},#{password},#{role},#{username},#{dormBuilding},#{dormRoom})")
    int insertUser(User user);

    //登录
    @Select("SELECT * FROM user WHERE account = #{account} AND password = #{password} AND role = #{role}")
    User selectUserByLogin(@Param("account") String account,
                           @Param("password") String password,
                           @Param("role") Integer role);

    //修改密码
    @Update("UPDATE user SET password=#{pwd} WHERE account=#{account}")
    void updatePwd(@Param("account") String account, @Param("pwd") String pwd);

    // 根据账号更新宿舍
    @Update("UPDATE user SET dorm_building=#{dormBuilding}, dorm_room=#{dormRoom} WHERE account=#{account}")
    int updateDorm(
            @Param("account") String account,
            @Param("dormBuilding") String dormBuilding,
            @Param("dormRoom") String dormRoom
    );

}
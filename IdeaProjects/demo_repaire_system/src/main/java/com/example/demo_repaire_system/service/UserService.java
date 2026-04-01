package com.example.demo_repaire_system.service;

import com.example.demo_repaire_system.entity.Result;
import com.example.demo_repaire_system.entity.User;

public interface UserService {
    //注册
    String register(Integer role, String account, String password, String username, String dormBuilding, String dormRoom, String confirmPwd);
    //登录
    Result login(Integer role, String account, String password);
    //修改密码
    Result updatePwd(String account, String oldPwd, String newPwd);
    //查询宿舍
    User getUserByAccount(String account);
    //修改绑定宿舍信息
    Result updateDorm(String account, String newBuilding, String newRoom);
    //根据账号查询用户信息
    User selectUserByAccount(String account);
}

package com.example.demo_repaire_system.controller.RepairController;

import com.example.demo_repaire_system.entity.Result;
import com.example.demo_repaire_system.entity.User;
import com.example.demo_repaire_system.service.UserService;
import com.example.demo_repaire_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //注册
    @GetMapping("/register")
    @ResponseBody
    public String register(
            @RequestParam Integer role,         // 角色 1学生 2管理员
            @RequestParam String account,       // 学号/工号
            @RequestParam String password,      // 密码
            @RequestParam String username,      // 姓名
            @RequestParam(required = false) String dormBuilding, // 非必填
            @RequestParam(required = false) String dormRoom,
            @RequestParam String confirmPwd     // 确认密码
    ){
        if (!password.equals(confirmPwd)) {
            return "两次密码不一致！";
        }
        return userService.register(role, account, password, username, dormBuilding, dormRoom, confirmPwd);
    }

    //登录
    @PostMapping("/login")
    public Result login(
       @RequestParam Integer role,
       @RequestParam String account,
       @RequestParam String password
    ){
        return userService.login(role, account, password);
    }

    //更新宿舍
    @GetMapping("/update-dorm")
    @ResponseBody
    public Result updateDorm(
            @RequestParam String account,
            @RequestParam String newDormBuilding,
            @RequestParam String newDormRoom
    ){
        return userService.updateDorm(account, newDormBuilding, newDormRoom);
    }

    //修改密码
    @GetMapping("/updatePwd")
    public Result updatePwd(
            @RequestParam  String account,
            @RequestParam String oldPwd,
            @RequestParam String newPwd
    ){
        return userService.updatePwd(account, oldPwd, newPwd);
    }

    @GetMapping("/getDorm")
    public User getDorm(@RequestParam String account) {
        return userService.selectUserByAccount(account);
    }
}

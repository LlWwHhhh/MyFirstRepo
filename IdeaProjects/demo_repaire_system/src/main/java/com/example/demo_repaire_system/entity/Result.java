package com.example.demo_repaire_system.entity;

public class Result {
    private boolean success;//登录是否成功
    private String msg;//欢迎语句+失败的原因
    private User user;//用户对象
    private String token;//token

    // 成功
    public static Result success(String msg, User user,String token ) {
        Result r = new Result();
        r.success = true;
        r.msg = msg;
        r.user = user;
        r.token = token;
        return r;
    }

    // 成功方法重载
    public static Result success(String msg) {
        Result r = new Result();
        r.success = true;
        r.msg = msg;
        r.user = null;
        r.token = null;
        return r;
    }

    // 失败
    public static Result fail(String msg) {
        Result r = new Result();
        r.success = false;
        r.msg = msg;
        r.user = null;
        r.token = null;
        return r;
    }

    // getter setter
    public boolean isSuccess() {return success;}
    public void setSuccess(boolean success) {this.success = success;}
    public String getMsg() {return msg;}
    public void setMsg(String msg) {this.msg = msg;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}

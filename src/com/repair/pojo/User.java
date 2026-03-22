package com.repair.pojo;

import java.util.Date;

public class User {
    private String username;
    private Integer id;         // 主键ID
    private String account;    // 学号/工号
    private String password;   // 密码
    private Integer role;      // 角色：1-学生，2-管理员
    private String dormBuilding; // 宿舍楼栋（数据库字段是dorm_building，MyBatis会自动映射）
    private String dormRoom;   // 宿舍房间号
    private Date createTime;   // 创建时间
    private Date updateTime;   // 更新时间

    // 无参构造（必须有）
    public User() {}

    // 注册用的构造方法（简化版）
    public User(String account, String password, Integer role) {
        this.account = account;
        this.password = password;
        this.role = role;
    }

    // getter/setter（必须有，MyBatis要读取/赋值）
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }
    public String getDormBuilding() { return dormBuilding; }
    public void setDormBuilding(String dormBuilding) { this.dormBuilding = dormBuilding; }
    public String getDormRoom() { return dormRoom; }
    public void setDormRoom(String dormRoom) { this.dormRoom = dormRoom; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}

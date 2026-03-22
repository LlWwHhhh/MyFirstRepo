package com.repair.pojo;

import java.util.Date;

public class RepairOrder {
    private Integer id;
    private String studentAccount;  // 学生账号
    private String dormBuilding;    // 楼栋
    private String dormRoom;        // 房间号
    private String problem;         // 问题描述
    private String status;          // 状态：待处理 / 已处理
    private Date createTime;        // 创建时间

    public RepairOrder(){}

    public RepairOrder(String studentAccount, String dormBuilding, String dormRoom, String problem) {
        this.studentAccount = studentAccount;
        this.dormBuilding = dormBuilding;
        this.dormRoom = dormRoom;
        this.problem = problem;
        this.status = "待处理"; // 默认待处理
    }

    // getter 和 setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getStudentAccount() { return studentAccount; }
    public void setStudentAccount(String studentAccount) { this.studentAccount = studentAccount; }
    public String getDormBuilding() { return dormBuilding; }
    public void setDormBuilding(String dormBuilding) { this.dormBuilding = dormBuilding; }
    public String getDormRoom() { return dormRoom; }
    public void setDormRoom(String dormRoom) { this.dormRoom = dormRoom; }
    public String getProblem() { return problem; }
    public void setProblem(String problem) { this.problem = problem; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}

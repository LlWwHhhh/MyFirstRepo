package com.repair;

import com.repair.pojo.RepairOrder;
import com.repair.service.RepairService;
import com.repair.service.UserService;
import com.repair.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();
        RepairService repairService = new RepairService();
        String loginUserAccount = null;
        Integer loginUserRole = null;

        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            System.out.println("✅ 数据库连接成功！");
        } catch (Exception e) {
            System.out.println("❌ 数据库连接失败：" + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("===========================");
            System.out.println("🏠 宿舍报修管理系统");
            System.out.println("===========================");
            System.out.println("1. 登录");
            System.out.println("2. 注册");
            System.out.println("3. 退出");
            System.out.print("请选择操作（输入 1-3）：");

            String choiceStr = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("❌ 输入错误！请输入数字1-3");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("===== 用户登录 =====");
                    System.out.println("请选择登录角色（1-学生，2-教职工）：");
                    String loginRoleStr = scanner.nextLine();
                    int loginRole;
                    try {
                        loginRole = Integer.parseInt(loginRoleStr);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ 角色必须输入数字！");
                        break;
                    }

                    System.out.println("请输入学号/工号：");
                    String loginAccount = scanner.nextLine();

                    System.out.println("请输入密码：");
                    String loginPwd = scanner.nextLine();

                    String loginResult = userService.login(loginRole, loginAccount, loginPwd);
                    System.out.println(loginResult);

                    if (loginResult.startsWith("✅")) {
                        loginUserAccount = loginAccount;
                        loginUserRole = loginRole;
                        showUserMenu(scanner, loginUserRole, loginUserAccount, repairService, userService);
                    }
                    break;

                case 2:
                    System.out.println("===== 用户注册 =====");
                    System.out.print("请选择角色（1-学生，2-维修人员）：");
                    String roleStr = scanner.nextLine();
                    int role;
                    try {
                        role = Integer.parseInt(roleStr);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ 角色输入错误！");
                        break;
                    }

                    System.out.print("请输入学号/工号：");
                    String account = scanner.nextLine().trim();
                    System.out.print("请输入姓名：");
                    String name = scanner.nextLine().trim();
                    System.out.print("请输入密码：");
                    String pwd = scanner.nextLine().trim();
                    System.out.print("请确认密码：");
                    String confirmPwd = scanner.nextLine().trim();
                    String dormBuilding = null;
                    String dormroom = null;
                    if (role == 1) {
                        System.out.print("请输入宿舍楼栋：");
                        dormBuilding = scanner.nextLine().trim();
                        System.out.print("请输入宿舍号：");
                        dormroom = scanner.nextLine().trim();
                    }

                    String result = userService.register(role, account, pwd, name, dormBuilding, dormroom, confirmPwd);
                    System.out.println(result);
                    break;

                case 3:
                    System.out.println("👋 退出系统！");
                    scanner.close();
                    return;

                default:
                    System.out.println("❌ 输入错误！请选择1-3");
            }
        }
    }

    private static void showUserMenu(Scanner scanner, Integer role, String account, RepairService repairService, UserService userService) {
        while (true) {
            System.out.println("===========================");
            if (role == 1) {
                System.out.println("学生功能");
                System.out.println("1. 修改绑定宿舍");
                System.out.println("2. 提交报修");
                System.out.println("3. 查看我的报修");
                System.out.println("4. 删除报修订单");
                System.out.println("5. 修改密码");
                System.out.println("6. 返回主菜单");
            } else {
                System.out.println("教职工功能");
                System.out.println("1. 查看所有报修单");
                System.out.println("2. 更新报修状态");
                System.out.println("3. 删除报修单");
                System.out.println("4. 修改密码");
                System.out.println("5. 返回主菜单");
            }
            System.out.println("===========================");
            System.out.print("请选择：");

            String optStr = scanner.nextLine();
            int opt;
            try {
                opt = Integer.parseInt(optStr);
            } catch (Exception e) {
                System.out.println("❌ 请输入数字！");
                continue;
            }

            if (role == 1) {
                if(opt == 1){// 修改绑定宿舍
                    System.out.println("===== 修改绑定宿舍 =====");
                    System.out.print("请输入新的宿舍楼栋：");
                    String newBuilding = scanner.nextLine().trim();
                    System.out.print("请输入新的宿舍号：");
                    String newRoom = scanner.nextLine().trim();
                    if (newBuilding.isEmpty() || newRoom.isEmpty()) {
                        System.out.println("❌ 楼栋和房间号不能为空！");
                    } else {
                        // 调用UserService的修改宿舍方法
                        String updateResult = userService.updateDorm(account, newBuilding, newRoom);
                        System.out.println(updateResult);
                    }

                }else if (opt == 2) {
                    System.out.println("===== 提交报修 =====");
                    System.out.print("请输入问题描述：");
                    String problem = scanner.nextLine();
                    String res = repairService.addRepairOrder(account, problem);
                    System.out.println(res);
                } else if (opt == 3) {
                    System.out.println("===== 我的报修单 =====");
                    List<RepairOrder> list = repairService.getMyOrders(account);
                    for (RepairOrder o : list) {
                        System.out.println("单号：" + o.getId()
                                + " | 楼栋：" + o.getDormBuilding()
                                + " | 房间：" + o.getDormRoom()
                                + " | 状态：" + o.getStatus()
                                + " | 问题：" + o.getProblem()
                                + " | 报修时间：" + o.getCreateTime()
                        );
                    }
                } else if (opt == 4) {
                    System.out.print("请输入要删除的订单号：");
                    int id = Integer.parseInt(scanner.nextLine());
                    String msg = repairService.deleteByStudent(id, account);
                    System.out.println(msg);
                } else if (opt == 5) {
                    System.out.print("请输入原密码：");
                    String oldPwd = scanner.nextLine();
                    System.out.print("请输入新密码：");
                    String newPwd = scanner.nextLine();
                    String msg = userService.updatePwd(account, oldPwd, newPwd);
                    System.out.println(msg);
                } else if (opt == 6) {
                    System.out.println("✅ 返回主菜单");
                    return;
                } else {
                    System.out.println("❌ 无效选项！");
                }
            } else {
                if (opt == 1) {
                    System.out.println("===== 所有报修单 =====");
                    List<RepairOrder> list = repairService.getAllOrders();
                    for (RepairOrder o : list) {
                        System.out.println("单号：" + o.getId()
                                + " | 学生：" + o.getStudentAccount()
                                + " | 楼栋：" + o.getDormBuilding()
                                + " | 房间：" + o.getDormRoom()
                                + " | 状态：" + o.getStatus()
                                + " | 问题：" + o.getProblem()
                                + " | 报修时间：" + o.getCreateTime());
                    }
                } else if (opt == 2) {
                    System.out.print("输入要更新的订单号：");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("输入新状态（例如：已处理）：");
                    String status = scanner.nextLine();
                    String msg = repairService.updateStatus(id, status);
                    System.out.println(msg);
                } else if (opt == 3) {
                    System.out.print("输入要删除的订单号：");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("确定删除？(yes/no)：");
                    String confirm = scanner.nextLine();
                    String msg = repairService.deleteByAdmin(id, confirm.equalsIgnoreCase("yes"));
                    System.out.println(msg);
                } else if (opt == 4) {
                    System.out.print("原密码：");
                    String oldPwd = scanner.nextLine();
                    System.out.print("新密码：");
                    String newPwd = scanner.nextLine();
                    String msg = userService.updatePwd(account, oldPwd, newPwd);
                    System.out.println(msg);
                } else if (opt == 5) {
                    System.out.println("✅ 返回主菜单");
                    return;
                } else {
                    System.out.println("❌ 无效选项！");
                }
            }
        }
    }
}
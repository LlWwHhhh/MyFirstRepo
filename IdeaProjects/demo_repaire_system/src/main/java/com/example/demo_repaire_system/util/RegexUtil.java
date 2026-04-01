package com.example.demo_repaire_system.util;

import java.util.regex.Pattern;

public class RegexUtil {
    // 学生学号正则：3125或3225开头，后面跟6位数字（比如3125004123）
    private static final String STUDENT_ACCOUNT_REGEX = "^(3125|3225)\\d{6}$";
    // 管理员工号正则：0025开头，后面跟6位数字（比如0025004123）
    private static final String ADMIN_ACCOUNT_REGEX = "^0025\\d{6}$";

    // 校验学生账号
    public static boolean isStudentAccount(String account) {
        return Pattern.matches(STUDENT_ACCOUNT_REGEX, account);
    }

    // 校验管理员账号
    public static boolean isAdminAccount(String account) {
        return Pattern.matches(ADMIN_ACCOUNT_REGEX, account);
    }
}

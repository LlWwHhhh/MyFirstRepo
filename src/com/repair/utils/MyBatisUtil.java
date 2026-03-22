package com.repair.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtil {
    private static SqlSessionFactory sqlSessionFactory;

    // 静态代码块加载配置
    static {
        try {
            // 读取MyBatis核心配置文件
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("MyBatis配置加载失败！");
        }
    }

    // 获取SqlSession（自动提交事务，新手友好）
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession(true);
    }
}

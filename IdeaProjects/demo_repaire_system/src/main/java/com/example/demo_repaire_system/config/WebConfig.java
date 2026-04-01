package com.example.demo_repaire_system.config;

import com.example.demo_repaire_system.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 读取配置文件里的图片保存路径
    @Value("${file.upload.path}")
    private String uploadPath;

    // 读取访问前缀
    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    // 关键：映射访问路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(urlPrefix + "**")
                .addResourceLocations("file:" + uploadPath);
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                // 拦截哪些请求（比如所有 /repair/ 开头的）
                .addPathPatterns("/repair/**")
                // 排除哪些请求（登录接口要排除，因为还没有 Token）
                .excludePathPatterns("/user/login");
    }
}
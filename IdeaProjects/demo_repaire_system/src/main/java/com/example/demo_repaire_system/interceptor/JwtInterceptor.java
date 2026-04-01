package com.example.demo_repaire_system.interceptor;

import com.example.demo_repaire_system.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtInterceptor implements HandlerInterceptor {

    /**
     * 预处理：在 Controller 执行之前执行
     * return true: 放行，继续执行接口
     * return false: 拦截，拒绝执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 从请求头里拿 Token
        // 前端通常会放在 Authorization 头里，或者叫 token
        String token = request.getHeader("token");

        // 2. 判断 Token 是否存在
        if (token == null || token.isEmpty()) {
            // 返回 401 未授权
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write("{\"success\":false,\"message\":\"请先登录！Token 为空！\"}");
            out.flush();
            out.close();
            return false; // 拦截！不往下走
        }

        try {
            // 3. 调用工具类解析 Token。如果解析失败（比如过期或被篡改），会抛异常
            JwtUtil.validateTokenAndGetClaims(token);

            // 4. 解析成功！说明 Token 是合法的
            System.out.println("Token 校验成功，放行！");
            return true;
        } catch (ExpiredJwtException e) {
            // Token 过期了
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write("{\"success\":false,\"message\":\"登录状态已过期，请重新登录！\"}");
            out.flush();
            out.close();
            return false;
        } catch (SignatureException | MalformedJwtException e) {
            // Token 签名错误 / 被篡改
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write("{\"success\":false,\"message\":\"登录信息无效！\"}");
            out.flush();
            out.close();
            return false;
        }
    }

    // 下面两个方法不用管，留空即可
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}
}
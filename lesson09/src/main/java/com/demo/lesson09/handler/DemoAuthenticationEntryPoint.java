package com.demo.lesson09.handler;

import com.alibaba.fastjson.JSON;
import com.demo.lesson09.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class DemoAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     *
     * @param request 请求request
     * @param response 请求response
     * @param authException 认证的异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // authException.getLocalizedMessage()返回本地化语言的错误信息
        Result<String> result = Result.failed(authException.getLocalizedMessage());
        String json = JSON.toJSONString(result);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}

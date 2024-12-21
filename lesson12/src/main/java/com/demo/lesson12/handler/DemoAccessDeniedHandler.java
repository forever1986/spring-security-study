package com.demo.lesson12.handler;

import com.alibaba.fastjson.JSON;
import com.demo.lesson12.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class DemoAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // accessDeniedException.getLocalizedMessage()返回本地化语言的错误信息
        Result<String> result = Result.failed(accessDeniedException.getLocalizedMessage());
        String json = JSON.toJSONString(result);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}

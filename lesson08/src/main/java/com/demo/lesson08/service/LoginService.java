package com.demo.lesson08.service;

import com.demo.lesson08.entity.LoginDTO;
import com.demo.lesson08.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    // 注入AuthenticationManagerBuilder，用于获得authenticationManager
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    public Result<String> login(LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        // 拿到前端传过来的用户名密码
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        // 构建一个AuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        try {
            // 以下部分是因为我们屏蔽原先密码登录，自己实现密码登录，因此要模仿UsernamePasswordAuthenticationFilter处理流程
            // 使用authenticationManager认证
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 成功返回登录成功
            if(authentication!=null && authentication.isAuthenticated()){
                // 保存用户信息
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // 设置session
                HttpSession session = request.getSession();
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
                return Result.success("登录成功");
            }
        }catch (AuthenticationException e){
            // 返回认证失败信息
            return Result.failed(e.getLocalizedMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            return Result.failed("未知错误");
        }
        return Result.failed("认证失败");
    }

    public Result<String> logout() {
        // 判断SecurityContext
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            SecurityContextHolder.getContext().setAuthentication(null);
            // 清楚SecurityContext
            SecurityContextHolder.clearContext();
            return Result.success("登出成功");
        }else{
            return Result.failed("登出失败，用户不存在");
        }
    }
}

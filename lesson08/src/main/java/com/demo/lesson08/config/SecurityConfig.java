package com.demo.lesson08.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 所有访问都必须认证
                .authorizeHttpRequests(auth->auth
                        // 允许"/login","/logout"免登录访问
                        .requestMatchers("/login","/logout").permitAll()
                        // 其它接口需要登录访问
                        .anyRequest().authenticated())
                // 自定义登录界面，并允许无需认证即可访问
                .formLogin(AbstractHttpConfigurer::disable)
                // 关闭默认的登出
                .logout(LogoutConfigurer::disable)
                // 关闭csrf功能，因为我们有post请求，而csrf会屏蔽post请求
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}

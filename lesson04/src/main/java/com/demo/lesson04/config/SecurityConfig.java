package com.demo.lesson04.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 所有访问都必须认证
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                // 自定义登录界面，并允许无需认证即可访问
                .formLogin(formLoginConfigurer -> formLoginConfigurer.loginPage("/login").permitAll())
                // 关闭csrf功能，因为我们页面有post请求，csrf会屏蔽post请求
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}

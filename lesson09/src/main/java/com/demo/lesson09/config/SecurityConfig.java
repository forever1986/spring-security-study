package com.demo.lesson09.config;

import com.demo.lesson09.filter.JwtAuthenticationTokenFilter;
import com.demo.lesson09.handler.DemoAccessDeniedHandler;
import com.demo.lesson09.handler.DemoAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->auth
                        //允许/login访问
                        .requestMatchers("/login").permitAll().anyRequest().authenticated())
                // 异常处理
                .exceptionHandling(handling-> handling
                        .accessDeniedHandler(new DemoAccessDeniedHandler())
                        .authenticationEntryPoint(new DemoAuthenticationEntryPoint()))
                // 禁用csrf，因为登录和登出是post请求，csrf会屏蔽掉post请求
                .csrf(AbstractHttpConfigurer::disable)
                // 添加到过滤器链路中，确保在AuthorizationFilter过滤器之前
                .addFilterBefore(jwtAuthenticationTokenFilter, AuthorizationFilter.class)
                // 由于采用token方式认证，因此可以关闭session管理
                .sessionManagement(SessionManagementConfigurer::disable)
                // 禁用原来登录页面
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁用系统原有的登出
                .logout(LogoutConfigurer::disable);
        return http.build();
    }
}

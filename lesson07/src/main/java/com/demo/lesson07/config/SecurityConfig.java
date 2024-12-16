package com.demo.lesson07.config;

import com.demo.lesson07.handler.DemoAccessDeniedHandler;
import com.demo.lesson07.handler.DemoAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 所有访问都必须认证
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/demo").hasRole("admin")
                        .anyRequest().authenticated())
                // 默认配置
                .formLogin(Customizer.withDefaults())
                // 配置异常处理
                .exceptionHandling(handling -> handling
                        // 认证异常处理 -- 测试授权异常时，可以注释掉DemoAuthenticationEntryPoint
                        .authenticationEntryPoint(new DemoAuthenticationEntryPoint())
                        // 授权异常处理
                        .accessDeniedHandler(new DemoAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService(){
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return User.withUsername("test")
                        .password("{noop}1234")
                        .roles("user")
                        .build();
            }
        };
    }
}

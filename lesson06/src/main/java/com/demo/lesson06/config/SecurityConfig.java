package com.demo.lesson06.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@EnableRedisIndexedHttpSession // 为findByIndexNameSessionRepository做一个默认实现类
public class SecurityConfig {

    @Autowired
    FindByIndexNameSessionRepository findByIndexNameSessionRepository;

    @Bean
    public SessionRegistry sessionRegistry(){
        SessionRegistry sessionRegistry = new SpringSessionBackedSessionRegistry<>(findByIndexNameSessionRepository);
        return sessionRegistry;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 所有访问都必须认证
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                .sessionManagement(session -> session
                        // 配置最大客户端为1，且以第一次登录为主
                        .maximumSessions(1).maxSessionsPreventsLogin(true)
                        // 自定义Session的存储方式
                        .sessionRegistry(sessionRegistry())
                )
                // 默认配置
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

}

package com.demo.lesson05.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity  // 在使用注解授权时，需要加入该注解
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 所有访问都必须认证
                .authorizeHttpRequests(auth->auth
                        // 访问manager方法，需要具备manager权限
                        .requestMatchers("/manager").hasAuthority("manager")
                        // 访问operate下所有子方法，需要具备operate权限
                        .requestMatchers("/operate/**").hasAuthority("operate")
                        // 访问info方法，只需要具备info、operate或者manager权限都可以访问
                        .requestMatchers("/info").hasAnyAuthority("info","operate", "manager")
                        // 其它访问都必须认证
                        .anyRequest().authenticated())
                // 默认配置
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService(){
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return User.withUsername("test")
                        .password("{noop}1234")
                        .authorities("info")
                        .build();
            }
        };
    }

}

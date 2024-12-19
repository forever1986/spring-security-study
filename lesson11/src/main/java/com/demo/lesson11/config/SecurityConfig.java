package com.demo.lesson11.config;

import com.demo.lesson11.properties.IgnoreUrlsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableConfigurationProperties(IgnoreUrlsProperties.class)
public class SecurityConfig {

    @Autowired
    private IgnoreUrlsProperties ignoreUrlsProperties;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 所有访问都必须认证
                .authorizeHttpRequests(auth->auth
                        // 加入免鉴权白名单
                        .requestMatchers(ignoreUrlsProperties.getRequestMatcher()).permitAll()
                        // 其它访问都必须认证
                        .anyRequest().authenticated())
                // 禁用csrf，因为登录和登出是post请求，csrf会屏蔽掉post请求
                .csrf(AbstractHttpConfigurer::disable)
                // 默认配置
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    /**
     * 默认一个用户
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService(){
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return User.withUsername("test")
                        .password("{noop}1234")
                        .build();
            }
        };
    }

}

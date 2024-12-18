package com.demo.lesson10.config;

import com.demo.lesson10.authentication.PhoneCodeAuthenticationFilter;
import com.demo.lesson10.authentication.PhoneCodeAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 定义手机验证码需要的phoneCodeAuthenticationProvider
     */
    @Bean
    public PhoneCodeAuthenticationProvider phoneCodeAuthenticationProvider() {
        return new PhoneCodeAuthenticationProvider();
    }

    /**
     * 定义密码加密方式，用于DaoAuthenticationProvider
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 定义AuthenticationManager，加入两种AuthenticationProvider
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        // 保留原来账号密码登录的AuthenticationProvider
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // 将daoAuthenticationProvider和 phoneCodeAuthenticationProvider 加入到authenticationManager
        return new ProviderManager(daoAuthenticationProvider, phoneCodeAuthenticationProvider());
    }

    /**
     * 定义securityContextRepository，加入两种securityContextRepository
     */
    @Bean
    public SecurityContextRepository securityContextRepository(){
        HttpSessionSecurityContextRepository httpSecurityRepository = new HttpSessionSecurityContextRepository();
        DelegatingSecurityContextRepository defaultRepository = new DelegatingSecurityContextRepository(
                httpSecurityRepository, new RequestAttributeSecurityContextRepository());
        return defaultRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 定义PhoneCodeAuthenticationFilter，将authenticationManager和securityContextRepository设置进去，保持一致
        PhoneCodeAuthenticationFilter phoneCodeAuthenticationFilter = new PhoneCodeAuthenticationFilter(authenticationManager());
        phoneCodeAuthenticationFilter.setSecurityContextRepository(securityContextRepository());
        http
                // 所有请求都需要认证
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                // 禁用csrf，因为登录和登出是post请求，csrf会屏蔽掉post请求
                .csrf(AbstractHttpConfigurer::disable)
                // 添加到过滤器链路中，确保在AuthorizationFilter过滤器之前
                .addFilterBefore(phoneCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 默认登录页面
                .formLogin(Customizer.withDefaults())
                // 设置全局authenticationManager
                .authenticationManager(authenticationManager())
                // 设置全局securityContextRepository
                .securityContext(c->c.securityContextRepository(securityContextRepository()))
        ;
        return http.build();
    }

    /**
     * 定义一个内存用户
     */
    public UserDetailsService userDetailsService(){
        return new UserDetailsService(){
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return User.withUsername("test")
                        .password("1234")
                        .build();
            }
        };
    }

}

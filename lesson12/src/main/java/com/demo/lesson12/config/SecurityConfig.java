package com.demo.lesson12.config;

import com.demo.lesson12.authentication.PhoneCodeAuthenticationProvider;
import com.demo.lesson12.filter.JwtAuthenticationTokenFilter;
import com.demo.lesson12.handler.DemoAccessDeniedHandler;
import com.demo.lesson12.handler.DemoAuthenticationEntryPoint;
import com.demo.lesson12.mapper.TUserMapper;
import com.demo.lesson12.properties.IgnoreUrlsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(IgnoreUrlsProperties.class)
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IgnoreUrlsProperties ignoreUrlsProperties;

    @Bean
    public PhoneCodeAuthenticationProvider phoneCodeAuthenticationProvider() {
        return new PhoneCodeAuthenticationProvider(tUserMapper, redisTemplate);
    }

    /**
     * 定义密码加密方式，用于DaoAuthenticationProvider
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 定义AuthenticationManager，加入两种AuthenticationProvider
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        // 保留原来账号密码登录的AuthenticationProvider
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // 将daoAuthenticationProvider和 phoneCodeAuthenticationProvider 加入到authenticationManager
        return new ProviderManager(daoAuthenticationProvider, phoneCodeAuthenticationProvider());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->auth
                        //允许/login访问
                        .requestMatchers("/login", "/loginphone").permitAll()
                        // 加入免鉴权白名单
                        .requestMatchers(ignoreUrlsProperties.getRequestMatcher()).permitAll()
                        .anyRequest().authenticated())
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
                .logout(LogoutConfigurer::disable)
                // 设置全局authenticationManager
                .authenticationManager(authenticationManager());
        return http.build();
    }
}

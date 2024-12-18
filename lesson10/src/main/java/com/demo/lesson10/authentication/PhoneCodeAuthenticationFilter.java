package com.demo.lesson10.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

/**
 * 手机验证码验证过滤器
 */
public class PhoneCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    // 前端手机参数
    public static final String SPRING_SECURITY_FORM_PHONE_KEY = "phone";
    // 前端手机验证码参数
    public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";

    // 是否只能为post请求
    private boolean postOnly = true;

    // 只拦截loginphonecode路径的POST方法
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/loginphonecode",
            "POST");

    public PhoneCodeAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public PhoneCodeAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(postOnly && !request.getMethod().equals("POST") ){
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }else{
            // 获取电话号码
            String phone = getPhone(request);
            // 获取验证码
            String code= getCode(request);
            // 组装AuthenticationToken
            PhoneCodeAuthenticationToken token = new PhoneCodeAuthenticationToken(phone,code);
            this.setDetails(request,token);
            return this.getAuthenticationManager().authenticate(token);
        }
    }

    /**
     * 将request传递到token中
     */
    public void setDetails(HttpServletRequest request , PhoneCodeAuthenticationToken token ){
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    /**
     * 获取前端的Phone
     */
    public String getPhone(HttpServletRequest request ){
        return request.getParameter(SPRING_SECURITY_FORM_PHONE_KEY);
    }

    /**
     * 获取前端的验证码
     */
    public String getCode(HttpServletRequest request ){
        return request.getParameter(SPRING_SECURITY_FORM_CODE_KEY);
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }
}

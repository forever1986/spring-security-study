package com.demo.lesson10.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;

public class PhoneCodeAuthenticationProvider implements AuthenticationProvider {

    /**
     * 支持PhoneCodeAuthenticationToken认证
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    /**
     * 认证
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        PhoneCodeAuthenticationToken token = (PhoneCodeAuthenticationToken) authentication;
        // 这里一般会通过电话号码查询用户，判断用户是否存在以及获取其权限，这里只是演示，就不做这一步
        String phone = (String) token.getPrincipal();
        if (phone == null) {
            throw new BadCredentialsException("无法获取电话信息");
        }
        String code = (String) token.getCredentials();
        if (code == null) {
            throw new BadCredentialsException("验证码为空");
        }
        // 这里只是演示，正常是有一个短信发送服务，发送短信后，我们将验证码存入Redis，然后根据电话号码去取这个验证码做验证。由于我们没有发送短信服务，这里就模拟一个固定验证码
        if(!"8633".equals(code)){
            throw new BadCredentialsException("验证码错误");
        }
        // 这里PhoneCodeAuthenticationToken第三个入参应该是权限，这里没有使用数据库，就默认一个空值。完整情况应该是前面通过phone获取到用户，再找到权限
        PhoneCodeAuthenticationToken result =
                new PhoneCodeAuthenticationToken(phone, code, new ArrayList<>());
        result.setDetails(token.getDetails());
        return result;
    }

}

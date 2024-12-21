package com.demo.lesson12.authentication;

import com.demo.lesson12.entity.TUser;
import com.demo.lesson12.mapper.TUserMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;

public class PhoneCodeAuthenticationProvider implements AuthenticationProvider {

    private TUserMapper tUserMapper;

    private RedisTemplate redisTemplate;

    private static String PRE_PHONE_KEY = "phone:";

    public PhoneCodeAuthenticationProvider(TUserMapper tUserMapper, RedisTemplate redisTemplate) {
        this.tUserMapper = tUserMapper;
        this.redisTemplate = redisTemplate;
    }

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
        // 判断电话是否为空
        String phone = (String) token.getPrincipal();
        if (phone == null) {
            throw new BadCredentialsException("无法获取电话信息");
        }
        String code = (String) token.getCredentials();
        if (code == null) {
            throw new BadCredentialsException("验证码为空");
        }
        // 这里应该是从另外发送短信服务中存入验证码
        // redisTemplate.opsForValue().set(PRE_PHONE_KEY + phone, "8633", 1000*60*5L, TimeUnit.MILLISECONDS);
        try {
            // 从Redis中获取预先存入的验证码
            Object object = redisTemplate.opsForValue().get(PRE_PHONE_KEY + phone);
            if (!object.equals(code)) {
                throw new BadCredentialsException("验证码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("验证码无效");
        }

        TUser tUser = tUserMapper.selectByPhone(phone);
        if( tUser == null){
            throw new BadCredentialsException("找不到用户");
        }
        // 这里PhoneCodeAuthenticationToken第三个入参应该是权限，这里没有使用数据库，就默认一个空值。完整情况应该是前面通过phone获取到用户，再找到权限
        PhoneCodeAuthenticationToken result =
                new PhoneCodeAuthenticationToken(tUser, code, new ArrayList<>());
        result.setDetails(token.getDetails());
        return result;
    }

}

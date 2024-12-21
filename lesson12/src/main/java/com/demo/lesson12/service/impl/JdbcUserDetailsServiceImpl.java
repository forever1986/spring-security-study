package com.demo.lesson12.service.impl;

import com.demo.lesson12.entity.LoginUserDetails;
import com.demo.lesson12.entity.TUser;
import com.demo.lesson12.mapper.TPermissionMapper;
import com.demo.lesson12.mapper.TUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JdbcUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TPermissionMapper tPermissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询自己数据库的用户信息
        TUser user = tUserMapper.selectByUsername(username);
        log.info("load user={}", user);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        List<String> list = tPermissionMapper.selectPermissionByUserId(user.getId());
        return new LoginUserDetails(user, list);
    }

}

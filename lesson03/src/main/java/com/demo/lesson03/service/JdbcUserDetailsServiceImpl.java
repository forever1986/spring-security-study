package com.demo.lesson03.service;

import com.demo.lesson03.entity.TUser;
import com.demo.lesson03.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JdbcUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询自己数据库的用户信息
        TUser user = tUserMapper.selectByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return User.builder().username(user.getUsername()).password(user.getPassword()).roles("USER").build();
    }

}

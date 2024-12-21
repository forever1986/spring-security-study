package com.demo.lesson12.mapper;

import com.demo.lesson12.entity.TUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TUserMapper {

    // 根据用户名，查询用户信息
    @Select("select * from t_user where username = #{username}")
    TUser selectByUsername(String username);

    // 根据电话号码，查询用户信息
    @Select("select * from t_user where phone = #{phone}")
    TUser selectByPhone(String phone);

}

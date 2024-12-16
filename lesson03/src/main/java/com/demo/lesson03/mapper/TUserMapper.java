package com.demo.lesson03.mapper;

import com.demo.lesson03.entity.TUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TUserMapper {

    // 根据用户名，查询用户信息
    @Select("select * from t_user where username = #{username}")
    TUser selectByUsername(String username);

}

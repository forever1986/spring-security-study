package com.demo.lesson12.mapper;

import com.demo.lesson12.entity.TUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TPermissionMapper {

    // 根据用户名，查询用户信息
    @Select("select distinct p.permission_code from t_user u\n" +
            "left join t_user_role ur on ur.user_id = u.id\n" +
            "left join t_role_permission rp on ur.role_id = rp.role_id \n" +
            "left join t_permission p on p.id = rp.permission_id \n" +
            "where u.id = #{id}")
    List<String> selectPermissionByUserId(Long id);

}

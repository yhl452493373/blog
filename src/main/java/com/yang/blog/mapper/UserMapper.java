package com.yang.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.blog.entity.User;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public interface UserMapper extends BaseMapper<User> {

    User findByUsername(String username);

    String findUsernameById(String id);
}

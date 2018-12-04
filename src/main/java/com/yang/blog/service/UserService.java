package com.yang.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.blog.entity.User;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public interface UserService extends IService<User> {
    void register(User user);

    User findByUsername(String username);

    String findUsernameById(String id);
}

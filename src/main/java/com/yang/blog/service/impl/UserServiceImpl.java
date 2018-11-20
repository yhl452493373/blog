package com.yang.blog.service.impl;

import com.yang.blog.entity.User;
import com.yang.blog.mapper.UserMapper;
import com.yang.blog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

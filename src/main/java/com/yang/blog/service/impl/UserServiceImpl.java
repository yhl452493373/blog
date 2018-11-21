package com.yang.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.config.SystemConfig;
import com.yang.blog.entity.BaseEntity;
import com.yang.blog.entity.User;
import com.yang.blog.exception.*;
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

    @Override
    public void register(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String confirmPassword = user.getConfirmPassword();
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNullException("用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw new PasswordNullException("密码不能为空");
        }
        if (username.length() < SystemConfig.getUsernameLength().getMin()) {
            throw new UsernameLengthException("用户名过短");
        }
        if (username.length() > SystemConfig.getUsernameLength().getMax()) {
            throw new UsernameLengthException("用户名过长");
        }
        if (password.length() < SystemConfig.getPasswordLength().getMin()) {
            throw new PasswordLengthException("密码过短");
        }
        if (password.length() > SystemConfig.getPasswordLength().getMax()) {
            throw new PasswordLengthException("密码过长");
        }
        if (!password.equals(confirmPassword)) {
            throw new PasswordNotSameException("两次输入的密码不一直");
        }
        String salt = CommonUtils.salt(SystemConfig.getSalt().getSize());
        Integer hashCount = SystemConfig.getSalt().getHashCount();
        user.setHashCount(hashCount);
        user.setPassword(CommonUtils.hashPassword(password, salt, hashCount));
        user.setSalt(salt);
        user.setAvailable(User.AVAILABLE);
        User existUser = baseMapper.findByUsername(username);
        if (existUser != null) {
            throw new UsernameExistException("用户已经存在");
        }
        baseMapper.insert(user);
    }

    @Override
    public User findByUsername(String username) {
        return baseMapper.findByUsername(username);
    }
}

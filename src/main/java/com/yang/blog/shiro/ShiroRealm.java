package com.yang.blog.shiro;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.User;
import com.yang.blog.exception.PasswordNullException;
import com.yang.blog.exception.UsernameNullException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm,在这里面比对用户信息(账号,密码等)
 *
 * @author 杨黄林
 */
public class ShiroRealm extends AuthorizingRealm {
    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //权限获取

        Set<String> permisssionSets = new HashSet<>();
        System.out.println("数据库查询权限");
        permisssionSets.add("index:view");

        info.setStringPermissions(permisssionSets);

        //角色获取
        Set<String> rolenames = new HashSet<>();
        System.out.println("数据库查询角色");
        rolenames.add("admin");

        info.addRoles(rolenames);

        return info;
    }

    /**
     * 权限认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //TODO 这里仅用于测试,实际使用根据token中的用户信息与数据库的进行比较,不一致则跑出相关异常.在登录接口中捕获这些异常进行返回提示
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        String password = String.valueOf(usernamePasswordToken.getPassword());

        if (StringUtils.isEmpty(username)) {
            throw new UsernameNullException("用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw new PasswordNullException("密码不能为空");
        }

        User user = ServiceConfig.serviceConfig.userService.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        String dbPassword = user.getPassword();
        String dbSalt = user.getSalt();

        if (!CommonUtils.validatePassword(password, dbPassword, dbSalt, user.getHashCount())) {
            throw new IncorrectCredentialsException("用户名密码不匹配");
        }

        return new SimpleAuthenticationInfo(user, usernamePasswordToken.getPassword(), getName());
    }
}
package com.yang.blog.shiro;

import com.yang.blog.entity.User;
import org.apache.shiro.SecurityUtils;

public class ShiroUtils {
    public static User getLoginUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }
}

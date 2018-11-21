package com.yang.blog.exception;

import org.apache.shiro.authc.AccountException;

/**
 * 用户名已经存在的异常
 */
@SuppressWarnings("unused")
public class UsernameExistException extends AccountException {
    private static final long serialVersionUID = 8456590149491451043L;

    public UsernameExistException() {
    }

    public UsernameExistException(String message) {
        super(message);
    }

    public UsernameExistException(Throwable cause) {
        super(cause);
    }

    public UsernameExistException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.yang.blog.exception;

import org.apache.shiro.authc.AccountException;

/**
 * 密码为空的异常
 */
@SuppressWarnings("unused")
public class PasswordNullException extends AccountException {
    private static final long serialVersionUID = 5626371048352705173L;

    public PasswordNullException() {
    }

    public PasswordNullException(String message) {
        super(message);
    }

    public PasswordNullException(Throwable cause) {
        super(cause);
    }

    public PasswordNullException(String message, Throwable cause) {
        super(message, cause);
    }
}

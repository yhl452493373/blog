package com.yang.blog.exception;

import org.apache.shiro.authc.AccountException;

/**
 * 密码太短的异常
 */
@SuppressWarnings("unused")
public class PasswordLengthException extends AccountException {
    private static final long serialVersionUID = -2497350253348345984L;

    public PasswordLengthException() {
    }

    public PasswordLengthException(String message) {
        super(message);
    }

    public PasswordLengthException(Throwable cause) {
        super(cause);
    }

    public PasswordLengthException(String message, Throwable cause) {
        super(message, cause);
    }
}

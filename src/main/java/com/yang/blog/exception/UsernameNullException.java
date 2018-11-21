package com.yang.blog.exception;

import org.apache.shiro.authc.AccountException;

/**
 * 用户名为空的异常
 */
@SuppressWarnings("unused")
public class UsernameNullException extends AccountException {
    private static final long serialVersionUID = -1055488589105430943L;

    public UsernameNullException() {
    }

    public UsernameNullException(String message) {
        super(message);
    }

    public UsernameNullException(Throwable cause) {
        super(cause);
    }

    public UsernameNullException(String message, Throwable cause) {
        super(message, cause);
    }
}

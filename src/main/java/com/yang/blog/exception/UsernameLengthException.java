package com.yang.blog.exception;

import org.apache.shiro.authc.AccountException;

/**
 * 用户名太短的异常
 */
@SuppressWarnings("unused")
public class UsernameLengthException extends AccountException {
    private static final long serialVersionUID = -4086977036455581843L;

    public UsernameLengthException() {
    }

    public UsernameLengthException(String message) {
        super(message);
    }

    public UsernameLengthException(Throwable cause) {
        super(cause);
    }

    public UsernameLengthException(String message, Throwable cause) {
        super(message, cause);
    }
}

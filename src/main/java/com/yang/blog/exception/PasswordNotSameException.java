package com.yang.blog.exception;

import org.apache.shiro.authc.AccountException;

public class PasswordNotSameException extends AccountException {
    private static final long serialVersionUID = -8739237334990171798L;

    public PasswordNotSameException() {
    }

    public PasswordNotSameException(String message) {
        super(message);
    }

    public PasswordNotSameException(Throwable cause) {
        super(cause);
    }

    public PasswordNotSameException(String message, Throwable cause) {
        super(message, cause);
    }
}

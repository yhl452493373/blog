package com.yang.blog.exception;

import org.apache.shiro.authc.AccountException;

public class CaptchaNullException extends AccountException {
    private static final long serialVersionUID = 7591263180680510546L;

    public CaptchaNullException() {
    }

    public CaptchaNullException(String message) {
        super(message);
    }

    public CaptchaNullException(Throwable cause) {
        super(cause);
    }

    public CaptchaNullException(String message, Throwable cause) {
        super(message, cause);
    }
}

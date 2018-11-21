package com.yang.blog.exception;

import org.apache.shiro.authc.AccountException;

/**
 * 验证码输入错误的异常
 */
public class CaptchaErrorException extends AccountException {
    private static final long serialVersionUID = -6349271083451472554L;

    public CaptchaErrorException() {
    }

    public CaptchaErrorException(String message) {
        super(message);
    }

    public CaptchaErrorException(Throwable cause) {
        super(cause);
    }

    public CaptchaErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}

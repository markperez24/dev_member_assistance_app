package com.member.assistance.core.exception;

public class ReCaptchaException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public ReCaptchaException() {
        super();
    }

    public ReCaptchaException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaException(final String message) {
        super(message);
    }

    public ReCaptchaException(final Throwable cause) {
        super(cause);
    }

}

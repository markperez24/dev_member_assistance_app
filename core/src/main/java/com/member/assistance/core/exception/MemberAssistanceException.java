package com.member.assistance.core.exception;

public class MemberAssistanceException extends Exception {

    public MemberAssistanceException() {
        super();
    }

    public MemberAssistanceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MemberAssistanceException(final String message) {
        super(message);
    }

    public MemberAssistanceException(final Throwable cause) {
        super(cause);
    }

    public MemberAssistanceException(String s, String message, Exception e) {
    }
}

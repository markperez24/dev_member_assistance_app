package com.member.assistance.core.captcha;

import com.member.assistance.core.exception.ReCaptchaException;

import java.util.Map;

public interface CaptchaService {
    Map<String, Object> processResponse(final String response) throws ReCaptchaException;

    default void processResponse(final String response, String action) throws ReCaptchaException {}

    String getReCaptchaSite();

    String getReCaptchaSecret();

    Boolean isResponseValid(String captcha);
}

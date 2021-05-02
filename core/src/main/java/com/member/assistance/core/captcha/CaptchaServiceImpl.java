package com.member.assistance.core.captcha;

import com.member.assistance.backend.model.ReCaptcha;
import com.member.assistance.backend.repository.ReCaptchaRepository;
import com.member.assistance.core.exception.ReCaptchaException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class CaptchaServiceImpl extends AbstractCaptchaService {
    private static final Logger LOGGER = LogManager.getLogger(CaptchaServiceImpl.class);

    @Autowired
    private CaptchaSettings captchaSettings;

    @Autowired
    private RestOperations restTemplate;

    @Autowired
    private ReCaptchaRepository reCaptchaRepository;

    private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    @Override
    public Map<String, Object> processResponse(String response) throws ReCaptchaException {
        LOGGER.info("Process recaptcha client response...");
        try {
            Map<String, Object> resultMap = new HashMap<>();
            if (!responseSanityCheck(response)) {
                throw new ReCaptchaException("Response contains invalid characters");
            }

            URI verifyUri = URI.create(String.format(
                    "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
                    getReCaptchaSecret(), response, getClientIP()));

            GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
            if (!googleResponse.isSuccess()) {
                throw new ReCaptchaException("reCaptcha was not successfully validated");
            }

            /*ReCaptcha reCaptcha = new ReCaptcha();
            reCaptcha.setClientResponse(response);
            reCaptcha.setSuccess(googleResponse.isSuccess());
            reCaptchaRepository.save(reCaptcha);*/
            //TODO: Add cron to clean daily/weekly

            resultMap.put("success", googleResponse.isSuccess());
            return resultMap;
        } catch (ReCaptchaException rce) {
            throw rce;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ReCaptchaException("Failed processing reCaptcha. Please try again later.");
        }
    }

    public boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    @Override
    public Boolean isResponseValid(String captcha) {
        LOGGER.debug("Is response valid");
        Boolean retVal = Boolean.FALSE;
        try {

            ReCaptcha response = reCaptchaRepository.findByClientResponse(captcha);
            if(Objects.nonNull(response)) {
                retVal = Boolean.TRUE;
                reCaptchaRepository.delete(response);
                //TODO: CRON to delete captcha daily instead of delete on reg
                // for statictics and attacks analysis
            }
            return retVal;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ReCaptchaException("Failed processing reCaptcha. Please try again later.");
        }
    }
}

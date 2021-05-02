package com.member.assistance.backend.repository;

import com.member.assistance.backend.model.ReCaptcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReCaptchaRepository extends JpaRepository<ReCaptcha, Long> {
    ReCaptcha findByClientResponse(String captcha);
}

package com.member.assistance.backend.audit;

import org.springframework.stereotype.Service;

@Service
public interface AuditorAwareService {
    void setCurrentUser(String user);
}

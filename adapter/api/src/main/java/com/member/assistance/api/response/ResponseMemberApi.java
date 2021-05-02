package com.member.assistance.api.response;

import com.member.assistance.core.dto.MemberDto;

public class ResponseMemberApi {
    private String message;
    private String status;
    private MemberDto profile;

    public ResponseMemberApi(String status, String message, MemberDto memberDto) {
        this.status = status;
        this.message = message;
        this.profile = memberDto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MemberDto getProfile() {
        return profile;
    }

    public void setProfile(MemberDto profile) {
        this.profile = profile;
    }
}

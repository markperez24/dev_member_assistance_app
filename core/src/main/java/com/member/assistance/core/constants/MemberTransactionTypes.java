package com.member.assistance.core.constants;

public enum MemberTransactionTypes {
    CHANGE_PASSWORD("Change Password"),
    EDIT_PROFILE("Edit Profile"),
    MEDICAL_ASSISTANCE("Medical Assistance"),
    REGISTRATION("Registration");

    private String value;

    MemberTransactionTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package com.member.assistance.core.constants;

public enum Roles {
    ADMINISTRATOR("Administrator"),
    MEMBER("Member"),
    ADMINISTRATOR_SUPER("Administrator"),
    GUEST("guest");

    private String value;

    Roles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

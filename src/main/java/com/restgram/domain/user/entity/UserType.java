package com.restgram.domain.user.entity;

public enum UserType {
    CUSTOMER("CUSTOMER"), STORE("STORE");
    private String name;
    private UserType(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

}

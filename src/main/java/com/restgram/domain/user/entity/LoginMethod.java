package com.restgram.domain.user.entity;

public enum LoginMethod {
    DEFAULT("default"), KAKAO("kakao");
    private String name;
    private LoginMethod(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}

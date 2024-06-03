package com.restgram.domain.user.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String uid;
    private String name;
    private String email;
    private String nickname;
    private String profileImage;
    private LoginMethod loginMethod;
    private UserType type;

    public static OAuthAttributes of(String registrationId, String nameAttributeKey, Map<String, Object> attributes) {
        switch (registrationId) {
            case "kakao":
                return ofKakao(nameAttributeKey, attributes);
        }
        return ofKakao(nameAttributeKey, attributes);
    }

    private static OAuthAttributes ofKakao(String nameAttributeKey, Map<String, Object> attributes) {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        StringBuilder sb = new StringBuilder();
        sb.append("KA").append(attributes.get("id"));
        String uid = sb.toString();
        return OAuthAttributes.builder()
                .nameAttributeKey(nameAttributeKey)
                .uid(uid)
                .name(String.valueOf(properties.get("nickname")))
                .email(String.valueOf(account.get("email")))
                .nickname(String.valueOf(profile.get("nickname")))
                .profileImage(String.valueOf(profile.get("profile_image_url")))
                .type(UserType.CUSTOMER)
                .loginMethod(LoginMethod.KAKAO)
                .build();
    }

    public Customer toEntity() {
        return Customer.builder()
                .uid(uid)
                .nickname(this.nickname)
                .email(this.email)
                .profileImage(this.profileImage)
                .name(this.name)
                .loginMethod(this.loginMethod)
                .addressRange(0)
                .calendarAgree(false)
                .build();
    }

}

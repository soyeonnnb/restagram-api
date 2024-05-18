package com.restgram.domain.user.dto.request;

import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.LoginMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerJoinRequest {
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수값입니다.")
    private String nickname;

    @NotBlank(message = "이름은 필수값입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수값입니다.")
    private String phone;

    private String profileImage;


    public Customer of() {
        return Customer.builder()
                .email(email)
                .uid(email)
                .name(name)
                .profileImage(profileImage)
                .phone(phone)
                .nickname(nickname)
                .password(password)
                .loginMethod(LoginMethod.DEFAULT)
                .addressRange(0)
                .build();
    }
}

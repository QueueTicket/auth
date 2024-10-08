package com.qticket.auth.application.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInReqDto {
    private String email;
    private String password;
}

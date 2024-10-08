package com.qticket.auth.application.dtos.user;

import com.qticket.auth.domain.user.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateReqDto {
    @NotNull
    private String name;
    @NotNull
    private String phoneNumber;
    @NotNull
    private UserRole role;
}

package com.qticket.auth.application.dtos.user;

import com.qticket.auth.domain.user.User;
import com.qticket.auth.domain.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResDto {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private UserRole userRole;
    private LocalDateTime birthDate;

    public static UserResDto from(User user) {
        return UserResDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .userRole(user.getUserRole())
                .birthDate(user.getBirthDate())
                .build();
    }
}

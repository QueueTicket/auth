package com.qticket.auth.application.dtos.user;

import com.qticket.auth.domain.user.UserRole;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SignUpReqDto {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$",
            message = "Password must be 8 to 15 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    private UserRole role;

    private String name;

    private LocalDateTime birthDate;

    private String phoneNumber;
}

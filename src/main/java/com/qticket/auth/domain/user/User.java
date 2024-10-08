package com.qticket.auth.domain.user;

import com.qticket.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_delete is false")
@Entity(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDateTime birthDate;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    public User(String email, String encodedPassword, UserRole role, String name, LocalDateTime birthDate, String phoneNumber) {
        this.email = email;
        this.password = encodedPassword;
        this.userRole = role;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public void updateUserInfo(String name, String phoneNumber, UserRole userRole) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }
}

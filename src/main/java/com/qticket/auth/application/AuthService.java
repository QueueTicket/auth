package com.qticket.auth.application;

import com.qticket.auth.application.dtos.user.SignUpReqDto;
import com.qticket.auth.config.AuthErrorCode;
import com.qticket.auth.config.JwtTokenProvider;
import com.qticket.auth.domain.user.User;
import com.qticket.auth.domain.user.UserRepository;
import com.qticket.common.exception.ErrorCode;
import com.qticket.common.exception.QueueTicketException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    // 회원가입 로직
    public void registerUser(SignUpReqDto signUpReqDto) {
        // 사용자 이름 중복 확인
        if (userRepository.findByEmail(signUpReqDto.getEmail()).isPresent()) {
            throw new QueueTicketException(AuthErrorCode.USER_EMAIL_CONFLICT);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpReqDto.getPassword());

        // 새로운 사용자 저장
        User newUser = new User(signUpReqDto.getEmail(),
                encodedPassword,
                signUpReqDto.getRole(),
                signUpReqDto.getName(),
                signUpReqDto.getBirthDate(),
                signUpReqDto.getPhoneNumber()
        );

        userRepository.save(newUser);
    }

    // 로그인 로직
    public String login(String email, String password) {
        // 사용자 조회
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new QueueTicketException(AuthErrorCode.NOT_FOUND_ERROR);
        }

        User user = userOptional.get();

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new QueueTicketException(AuthErrorCode.NOT_FOUND_ERROR);
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUserRole().name());

        // 사용자 정보와 함께 Redis에 저장
        Map<String, String> tokenData = new HashMap<>();
        tokenData.put("userId", String.valueOf(user.getId()));
        tokenData.put("userRole", user.getUserRole().name());

        // Redis에 토큰과 사용자 정보를 캐싱 (해시 형태로 저장)
        redisTemplate.opsForHash().putAll(token, tokenData);

        // JWT 토큰 생성
        return token;
    }

//    public String resetPassword(String userId, String resetCode, String newPassword) {
//        String cacheKey = "Reset::" + userId;
//        String cachedResetCode = ((String) redisTemplate.opsForValue().get(cacheKey)).replaceAll("\"", "");
//        if (cachedResetCode == null) {
//            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
//        }
//        if (!cachedResetCode.equals(resetCode)) {
//            throw new CoreApiException(ErrorType.INVALID);
//        }
//
//        Optional<User> userOptional = userRepository.findByUserId(userId);
//        if (userOptional.isEmpty()) {
//            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
//        }
//
//        User user = userOptional.get();
//
//        String encodedPassword = passwordEncoder.encode(newPassword);
//        user.setPassword(encodedPassword);
//
//        userRepository.save(user);
//
//        redisTemplate.delete(cacheKey);
//
//        return newPassword;
//    }
}

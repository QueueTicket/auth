package com.qticket.auth.controller;

import com.qticket.auth.application.AuthService;
import com.qticket.auth.application.dtos.auth.AuthResponseDto;
import com.qticket.auth.application.dtos.user.SignInReqDto;
import com.qticket.auth.application.dtos.user.SignUpReqDto;
import com.qticket.auth.config.JwtTokenProvider;
import com.qticket.common.ResponseWrapperAdvice;
import com.qticket.common.dto.ResponseDto;
import com.qticket.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입 API
    @PostMapping("/signUp")
    public ResponseDto<?> registerUser(@RequestBody SignUpReqDto signUpReqDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseDto.error("INVALID");
        }
        authService.registerUser(signUpReqDto);
        return ResponseDto.success("가입 성공");
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody SignInReqDto signInReqDto) {
        String token = authService.login(signInReqDto.getEmail(), signInReqDto.getPassword());
        return ResponseDto.success(token);  // JWT 토큰 반환
    }

//    @PostMapping("/resetPassword")
//    public ApiResponse<?> resetPassword(@RequestBody ResetPWRequestDto resetPWRequestDto) {
//
//        try {
//            String response = authService.resetPassword(resetPWRequestDto.getUserId(),resetPWRequestDto.getResetCode(),resetPWRequestDto.getNewPassword());
//            return ApiResponse.success(response);
//        } catch (IllegalArgumentException e) {
//            return ApiResponse.error(ErrorType.INVALID);
//        }
//    }

    @PostMapping("/validate-token")
    public AuthResponseDto validateToken(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);  // "Bearer " 부분 제거

        boolean isValid = jwtTokenProvider.validateToken(token);
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        String userRole = jwtTokenProvider.getUserRoleFromToken(token);

        // 검증 결과와 함께 AuthResponse 반환
        return new AuthResponseDto(isValid, userId, userRole);
    }

    @GetMapping("/test")
    public ResponseDto<?> test(@RequestHeader("X-USER-ROLE") String userRole) {
        return ResponseDto.success("UserRole" + userRole);
    }


}

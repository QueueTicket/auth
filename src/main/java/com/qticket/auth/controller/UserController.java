package com.qticket.auth.controller;

import com.qticket.auth.application.UserService;
import com.qticket.auth.application.dtos.user.UserUpdateReqDto;
import com.qticket.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseDto<?> getUser(
            @PathVariable Long userId) {
        return ResponseDto.success("유저 INFO",userService.getUser(userId));
    }

    @GetMapping("/userId")
    public ResponseDto<?> getUserByUserId(
            @RequestHeader(name = "X-USER-ID", required = false) String userId) {
        return ResponseDto.success("유저 INFO",userService.getUserByEmail(userId));
    }

    @GetMapping
    public ResponseDto<?> searchUser(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "createdAt", name = "sort") String sort,
            @RequestParam(defaultValue = "DESC", name = "direction") String direction,
            @RequestParam(required = false, name = "keyword") String keyword
    ) {
        if ("ADMIN".equals(userRole)) {
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(direction), sort));
            return ResponseDto.success("유저 INFO",userService.searchUser(keyword, pageable));
        } else {
            return ResponseDto.error("Invalid user role");
        }
    }

    @PutMapping("/{userId}")
    public ResponseDto<?> updateUser(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @PathVariable(name = "userId") Long userId,
            @RequestBody UserUpdateReqDto userUpdateReqDto
    ) {
        if ("ADMIN".equals(userRole)) {
            return ResponseDto.success("유저 수정 완료.",userService.updateUser(userId, userUpdateReqDto));
        } else {
            return ResponseDto.error("Invalid user role");
        }

    }

    @DeleteMapping("/{userId}")
    public ResponseDto<?> deleteUser(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestHeader(name = "X-USER-ID", required = false) String headUserId,
            @PathVariable(name = "userId") Long userId
    ) {
        if ("ADMIN".equals(userRole)) {
            return ResponseDto.success(userService.deleteUser(userId, headUserId));
        } else {
            return ResponseDto.error("Invalid user role");
        }

    }
}

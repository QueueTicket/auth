package com.qticket.auth.application;

import com.qticket.auth.application.dtos.user.UserResDto;
import com.qticket.auth.application.dtos.user.UserUpdateReqDto;
import com.qticket.auth.config.AuthErrorCode;
import com.qticket.auth.domain.user.User;
import com.qticket.auth.domain.user.UserRepository;
import com.qticket.common.exception.QueueTicketException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "UserService")
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResDto getUser(Long userId) {
        return UserResDto.from(userRepository.findById(userId).orElseThrow(()->{
            log.error("존재하지 않는 User ID 입니다.");
            throw new QueueTicketException(AuthErrorCode.NOT_FOUND_ERROR);
        }));
    }

    public UserResDto getUserByEmail(String email) {
        return UserResDto.from(userRepository.findByEmail(email).orElseThrow(()->{
            log.error("존재하지 않는 EMAIL 입니다.");
            throw new QueueTicketException(AuthErrorCode.NOT_FOUND_ERROR);
        }));
    }

    public List<UserResDto> searchUser(String keyword, Pageable pageable) {
        try{
            if(keyword == null){
                return userRepository.findAllByNameContaining("", pageable).stream().map(UserResDto::from).toList();
            }
            return userRepository.findAllByNameContaining(keyword, pageable).stream().map(UserResDto::from).toList();
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new QueueTicketException(AuthErrorCode.DEFAULT_ERROR);
        }
    }


    public UserResDto updateUser(Long userId, UserUpdateReqDto userUpdateReqDto) {
        User user = userRepository.findById(userId).orElseThrow(()->{
            log.error("존재하지 않는 User ID 입니다.");
            throw new QueueTicketException(AuthErrorCode.NOT_FOUND_ERROR);
        });

        try{
            user.updateUserInfo(userUpdateReqDto.getName(),
                    userUpdateReqDto.getPhoneNumber(), userUpdateReqDto.getRole());

            User savedUser = userRepository.save(user);

            return UserResDto.from(savedUser);
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new QueueTicketException(AuthErrorCode.DEFAULT_ERROR);
        }
    }

    public String deleteUser(Long userId, String headUserId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("존재하지 않는 User ID 입니다.");
            throw new QueueTicketException(AuthErrorCode.NOT_FOUND_ERROR);
        });

        try{
            user.softDelete(headUserId);
            userRepository.save(user);
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new QueueTicketException(AuthErrorCode.DEFAULT_ERROR);
        }

        return "User " + user.getId() + " is Deleted";
    }


}

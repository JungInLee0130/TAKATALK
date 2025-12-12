package com.example.chat.user.service;

import com.example.chat.exception.CustomException;
import com.example.chat.exception.ErrorCode;
import com.example.chat.user.domain.UserCreateForm;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public SiteUser create (UserCreateForm userCreateForm){
        /*중복 회원 체크*/
        userRepository.findByEmail(userCreateForm.email())
                .ifPresent(error -> {
                    throw new CustomException(ErrorCode.DUPLICATED_USER);
                });

        SiteUser siteUser = SiteUser.createUser(userCreateForm);
        userRepository.save(siteUser);

        return siteUser;
    }

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    public SiteUser findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public boolean existsByEmail(String email) {
        log.info("existsByEmail");
        return userRepository.existsByEmail(email);
    }
}


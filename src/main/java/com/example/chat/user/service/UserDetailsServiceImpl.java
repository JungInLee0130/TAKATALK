package com.example.chat.user.service;

import com.example.chat.global.exception.ErrorCode;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SiteUser siteUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        return new CustomUserDetails(siteUser);
    }
}

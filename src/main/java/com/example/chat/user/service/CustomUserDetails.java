package com.example.chat.user.service;

import com.example.chat.user.entity.SiteUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails, Serializable {

    private Long id;          // PK
    private String email;       // 로그인 ID
    private String password;    // 로그인 password
    private boolean emailVerified;  // 이메일 인증여부
    private boolean isNonLocked;     // 계정 잠김 여부
    private String nickname;
    private Collection<GrantedAuthority> authorities;

    public CustomUserDetails(SiteUser siteUser) {
        this.id = siteUser.getId();
        this.email = siteUser.getEmail();
        this.password = siteUser.getPassword();
        this.emailVerified = true; // 이메일 인증기능 넣을거면 false
        this.isNonLocked = true;
        this.nickname = siteUser.getNickname();
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + siteUser.getRole().name())
        );
    }

    /*
    * 해당 유저의 권한 목록
    * */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);  // SSO 정책에 따라서 이메일이 중복된값이 대입될수있으므로 DB SiteUser의 PK로 설정
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (emailVerified && isNonLocked);
    }
}

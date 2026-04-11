package com.example.chat.user.entity;

import com.example.chat.user.domain.SiteUserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SiteUserTest {

    @Test
    @DisplayName("소셜 가입 사용자를 정상적으로 생성한다.")
    void createOAuth2_Success() {
        // given
        String username = "test@kakao.com";
        String nickname = "테스트닉네임";
        String email = "test@kakao.com";
        String provider = "KAKAO";
        String providerId = "12345678";
        String profile = "http://profile.image.url";

        // when
        SiteUser user = SiteUser.createOAuth2(username, nickname, email, provider, providerId, profile);

        // then
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getProvider()).isEqualTo(provider);
        assertThat(user.getProviderId()).isEqualTo(providerId);
        assertThat(user.getProfile()).isEqualTo(profile);
        assertThat(user.getPassword()).isNull(); // 소셜 사용자는 비밀번호가 없어야 함
        assertThat(user.getRole()).isEqualTo(SiteUserRole.USER);
    }
}

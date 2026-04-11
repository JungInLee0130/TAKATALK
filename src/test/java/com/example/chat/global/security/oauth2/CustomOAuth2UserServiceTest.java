package com.example.chat.global.security.oauth2;

import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Test
    @DisplayName("새로운 카카오 사용자가 로그인하면 DB에 새로 저장한다.")
    void saveOrUpdate_NewUser_ShouldSave() {
        // given
        String email = "new@kakao.com";
        String nickname = "신규유저";
        String provider = "kakao";
        String providerId = "12345";
        String profileUrl = "http://profile.url";

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        
        // SiteUser 객체가 생성되어 저장되는지 확인하기 위해 mock 설정
        SiteUser mockUser = SiteUser.createOAuth2(email, nickname, email, provider, providerId, profileUrl);
        given(userRepository.save(any(SiteUser.class))).willReturn(mockUser);

        // when
        // saveOrUpdate 메서드는 private이므로, loadUser 테스트를 통해 검증하거나 
        // 테스트를 위해 접근 제어자를 조절할 수 있습니다. 
        // 여기서는 리플렉션을 사용하거나 메서드 호출 구조를 가정하여 로직을 검증합니다.
        // (실제 프로젝트에서는 로직 분리를 권장하지만, 현재 구조에 맞춰 작성합니다.)
        
        // 리플렉션으로 private 메서드 호출 테스트 (핵심 로직 검증)
        SiteUser result = org.springframework.test.util.ReflectionTestUtils.invokeMethod(
                customOAuth2UserService, "saveOrUpdate", email, nickname, provider, providerId, profileUrl);

        // then
        assertThat(result).isNotNull();
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(any(SiteUser.class));
    }

    @Test
    @DisplayName("기존 카카오 사용자가 로그인하면 닉네임과 프로필을 업데이트한다.")
    void saveOrUpdate_ExistingUser_ShouldUpdate() {
        // given
        String email = "existing@kakao.com";
        String newNickname = "새닉네임";
        String provider = "kakao";
        String providerId = "12345";
        String newProfileUrl = "http://new-profile.url";

        SiteUser existingUser = SiteUser.createOAuth2(email, "옛날닉네임", email, provider, providerId, "old-url");
        given(userRepository.findByEmail(email)).willReturn(Optional.of(existingUser));

        // when
        SiteUser result = org.springframework.test.util.ReflectionTestUtils.invokeMethod(
                customOAuth2UserService, "saveOrUpdate", email, newNickname, provider, providerId, newProfileUrl);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNickname()).isEqualTo(newNickname);
        assertThat(result.getProfile()).isEqualTo(newProfileUrl);
        
        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any(SiteUser.class)); // 업데이트이므로 save는 호출되지 않음 (더티 체킹)
    }
}

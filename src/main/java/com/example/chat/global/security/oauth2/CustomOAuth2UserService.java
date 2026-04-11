package com.example.chat.global.security.oauth2;

import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 기본 OAuth2UserService를 통해 사용자 정보를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. 서비스 구분 (kakao, google 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. OAuth2 로그인 진행 시 키가 되는 필드값 (Primary Key와 같은 의미)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 4. 소셜 서비스별 데이터 파싱 (카카오 기준)
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        // 카카오 전용 파싱 (id, kakao_account 내부에 email, profile 등이 있음)
        String providerId = String.valueOf(attributes.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        
        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");

        // 5. DB 저장 또는 업데이트
        SiteUser user = saveOrUpdate(email, nickname, registrationId, providerId, profileImageUrl);

        // 6. SecurityContext에 저장할 OAuth2User 객체 반환
        return new DefaultOAuth2User(
                Collections.emptyList(), // 권한은 현재 USER로 고정 (필요시 확장)
                attributes,
                userNameAttributeName
        );
    }

    private SiteUser saveOrUpdate(String email, String nickname, String provider, String providerId, String profileImageUrl) {
        Optional<SiteUser> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            SiteUser user = userOptional.get();
            user.updateNickname(nickname);
            user.updateProfile(profileImageUrl);
            return user;
        } else {
            // 신규 가입
            SiteUser newUser = SiteUser.createOAuth2(
                    email, // username으로 email 사용
                    nickname,
                    email,
                    provider.toUpperCase(),
                    providerId,
                    profileImageUrl
            );
            return userRepository.save(newUser);
        }
    }
}

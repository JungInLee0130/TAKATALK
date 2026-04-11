package com.example.chat.global.security.oauth2;

import com.example.chat.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OAuth2SuccessHandlerTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oAuth2User;

    @InjectMocks
    private OAuth2SuccessHandler oauth2SuccessHandler;

    private final String FRONT_URL = "http://localhost:5173";

    @BeforeEach
    void setUp() {
        // @Value 주입값을 수동으로 설정
        ReflectionTestUtils.setField(oauth2SuccessHandler, "frontUrl", FRONT_URL);
    }

    @Test
    @DisplayName("소셜 로그인 성공 시 카카오 계정의 이메일로 JWT를 생성한다.")
    void onAuthenticationSuccess_ShouldGenerateJwtWithCorrectEmail() throws Exception {
        // given
        String email = "test@kakao.com";
        setUpMockUser(email);
        given(jwtProvider.generateToken(email)).willReturn("mock-token");
        given(response.encodeRedirectURL(anyString())).willAnswer(i -> i.getArgument(0));

        // when
        oauth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(jwtProvider).generateToken(email); // 정확한 이메일로 토큰 생성을 요청했는지 검증
    }

    @Test
    @DisplayName("소셜 로그인 성공 시 생성된 토큰을 쿼리 파라미터에 담아 프론트엔드로 리다이렉트한다.")
    void onAuthenticationSuccess_ShouldRedirectToCorrectUrl() throws Exception {
        // given
        String email = "test@kakao.com";
        String mockToken = "mock-jwt-token";
        setUpMockUser(email);
        given(jwtProvider.generateToken(email)).willReturn(mockToken);
        
        // DefaultRedirectStrategy 내부 로직 대응: encodeRedirectURL이 들어온 URL을 그대로 반환하게 함
        given(response.encodeRedirectURL(anyString())).willAnswer(i -> i.getArgument(0));

        // when
        oauth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        String expectedUrl = FRONT_URL + "/login/success?token=" + mockToken;
        verify(response).sendRedirect(expectedUrl);
    }

    private void setUpMockUser(String email) {
        Map<String, Object> kakaoAccount = new HashMap<>();
        kakaoAccount.put("email", email);
        
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("kakao_account", kakaoAccount);

        given(authentication.getPrincipal()).willReturn(oAuth2User);
        given(oAuth2User.getAttributes()).willReturn(attributes);
    }
}

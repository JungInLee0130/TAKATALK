package com.example.chat.global.config.security.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

// 인증실패시
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final String defaultFailureUrl = "/login";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMsg;
        String email = request.getParameter("email");

        if (exception instanceof BadCredentialsException) {
            // 비밀번호 불일치 또는 존재하지않는 사용자
            errorMsg = "유효하지않는 아이디 또는 비밀번호 입니다.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            // loadUserByUsername 등 서비스계층에서 발생한 오류(사용자를 찾을수 없을때 등)
            errorMsg = "가입된 사용자가 아닙니다.";
        } else {
            errorMsg = "알수없는 이유로 로그인에 실패했습니다.";
        }

        HttpSession session = request.getSession();
        session.setAttribute("email", email);
        session.setAttribute("loginErrorMsg", errorMsg);

        response.sendRedirect(defaultFailureUrl);
    }
}

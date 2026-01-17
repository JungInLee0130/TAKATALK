package com.example.chat.global;

import com.example.chat.global.file.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 외부경로에서 내부 파일을 볼수있게해주는 설정
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final FileStorageProperties fileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 브라우저에서 /profile-images/** 로 접근하면
        // 실제 파일 시스템의 uploadDir 경로에서 파일을 찾음
        registry.addResourceHandler("/profile-images/**")
                .addResourceLocations("file:///" + fileProperties.getUploadPath());
    }

    // 리액트 - 스프링부트 cors 에러 차단
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowCredentials(true);
    }
}

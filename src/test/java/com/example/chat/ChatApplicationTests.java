package com.example.chat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")     // application-test.yml 사용
class ChatApplicationTests {

    @Test
    void contextLoads() {
    }

}

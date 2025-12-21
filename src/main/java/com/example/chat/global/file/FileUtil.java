package com.example.chat.global.file;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FileUtil {

    public static String getEffectiveProfile(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "/images/meeng.png";
        }
        return "/profile-images/" + fileName;
    }
}

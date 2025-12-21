package com.example.chat.group;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public record createGroupRequest (@NotEmpty String name,
                                  MultipartFile profile){
}

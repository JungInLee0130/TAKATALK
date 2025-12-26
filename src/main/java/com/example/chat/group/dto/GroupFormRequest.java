package com.example.chat.group.dto;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public record GroupFormRequest(@NotEmpty String name,
                               MultipartFile profile){
}

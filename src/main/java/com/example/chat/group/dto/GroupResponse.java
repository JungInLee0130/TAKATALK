package com.example.chat.group.dto;

import com.example.chat.global.file.FileUtil;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupResponse {
    private Long id;
    private String name;

    private String profile;

    @Builder
    public GroupResponse(Long id, String name, String profile) {
        this.id = id;
        this.name = name;
        this.profile = FileUtil.getEffectiveProfile(profile);
    }
}

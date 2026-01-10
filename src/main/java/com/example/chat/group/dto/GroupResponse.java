package com.example.chat.group.dto;

import com.example.chat.global.file.FileUtil;
import com.example.chat.group.entity.Group;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupResponse {
    private Long id;
    private String name;

    private String profile;

    @Builder
    private GroupResponse(Long id, String name, String profile) {
        this.id = id;
        this.name = name;
        this.profile = FileUtil.getEffectiveProfile(profile);
    }

    public static GroupResponse from(Group group) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .profile(group.getProfile())
                .build();
    }
}

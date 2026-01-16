package com.example.chat.group.dto;

import com.example.chat.global.file.FileUtil;
import com.example.chat.group.entity.Group;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class GroupEditResponse {
    private Long groupId;
    private String name;
    private String profileUrl;

    @Builder
    private GroupEditResponse(Long groupId, String name, String profile) {
        this.groupId = groupId;
        this.name = name;
        this.profileUrl = FileUtil.getEffectiveProfile(profile);
    }

    public static GroupEditResponse from(Group group) {
        return GroupEditResponse.builder()
                .groupId(group.getId())
                .name(group.getName())
                .profile(group.getProfile())
                .build();
    }


}

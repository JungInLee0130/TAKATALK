package com.example.chat.group;

import lombok.Getter;

@Getter
public class GroupGetResponse {
    private Long id;
    private String name;

    private String profile;

    public GroupGetResponse(Long id, String name, String profile) {
        this.id = id;
        this.name = name;
        this.profile = profile;
    }
}

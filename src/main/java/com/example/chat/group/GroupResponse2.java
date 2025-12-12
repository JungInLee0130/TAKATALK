package com.example.chat.group;

import lombok.Getter;

@Getter
public class GroupResponse2 {
    private Long id;
    private String name;

    private String profile;

    public GroupResponse2(Long id, String name, String profile) {
        this.id = id;
        this.name = name;
        this.profile = profile;
    }
}

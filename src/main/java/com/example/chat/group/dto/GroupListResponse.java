package com.example.chat.group.dto;

import com.example.chat.group.entity.Groups;

import java.util.ArrayList;
import java.util.List;

public class GroupListResponse {
    private List<Groups> groupsList = new ArrayList<>();

    public GroupListResponse(List<Groups> groupsList) {
        this.groupsList = groupsList;
    }
}

package com.example.chat.group;

import java.util.ArrayList;
import java.util.List;

public class GroupListResponse {
    private List<Groups> groupsList = new ArrayList<>();

    public GroupListResponse(List<Groups> groupsList) {
        this.groupsList = groupsList;
    }
}

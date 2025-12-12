package com.example.chat.channel.dto;

import com.example.chat.friends.FriendsResponse;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class InviteChannelResponse {
    String groupName;
    String channelName;
    List<FriendsResponse> friendsResponses;

    @Builder
    public InviteChannelResponse(String groupName, String channelName, List<FriendsResponse> friendsResponses) {
        this.groupName = groupName;
        this.channelName = channelName;
        this.friendsResponses = friendsResponses;
    }
}

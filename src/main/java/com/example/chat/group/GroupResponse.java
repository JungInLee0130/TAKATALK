package com.example.chat.group;

import com.example.chat.category.dto.CategoryResponse;
import com.example.chat.channel.dto.ChannelResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GroupResponse {
    private List<CategoryResponse> categorizedChannels = new ArrayList<>();
    private List<ChannelResponse> uncategorizedChannels = new ArrayList<>();

    //private List<Visitors> visitorsList = new ArrayList<>();;

    public GroupResponse(List<CategoryResponse> categorizedChannels, List<ChannelResponse> uncategorizedChannels) {
        this.categorizedChannels = categorizedChannels;
        this.uncategorizedChannels = uncategorizedChannels;
    }



    /*public void setVisitorsList(List<Visitors> visitorsList) {
        this.visitorsList = visitorsList;
    }*/
}

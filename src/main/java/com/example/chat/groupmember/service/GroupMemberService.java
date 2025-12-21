package com.example.chat.groupmember.service;

import com.example.chat.group.dto.GroupGetResponse;
import com.example.chat.group.entity.Groups;
import com.example.chat.groupmember.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;

    public List<GroupGetResponse> getGroupList(Long siteUserId) {
        List<Groups> groupList = groupMemberRepository.findGroupBySiteUserId(siteUserId);

        return groupList.stream()
                .map(group -> GroupGetResponse.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .profile(group.getProfile())
                    .build())
                .collect(Collectors.toList());
    }



    /*public void enterChannel(Long visitorsId, Long channelId) {
        Visitors visitor = visitorsRepository.findById(visitorsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        visitor.getChannels().getId().equals(channelId);
    }*/
}

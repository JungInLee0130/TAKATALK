package com.example.chat.friends;

import com.example.chat.friends.entity.Friends;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    public List<FriendsResponse> getFriendsResponses(Long siteUserId) {
        List<Friends> friendsList = friendRepository.findBySiteUserId(siteUserId);

        List<FriendsResponse> responseList = friendsList.stream()
                .map(Friends::getSiteUser)
                .map(siteUser -> FriendsResponse.builder()
                        .nickname(siteUser.getNickname())
                        .build())
                .collect(Collectors.toList());

        return responseList;
    }
}

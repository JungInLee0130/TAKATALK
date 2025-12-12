package com.example.chat.friends;


import com.example.chat.friends.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friends, Long> {
    List<Friends> findBySiteUserId(Long siteUserId);
}

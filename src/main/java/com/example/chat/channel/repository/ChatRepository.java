package com.example.chat.channel.repository;

import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.entity.Chats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chats, Long> {
    Optional<Chats> findByChannelId(Long channelId);
}

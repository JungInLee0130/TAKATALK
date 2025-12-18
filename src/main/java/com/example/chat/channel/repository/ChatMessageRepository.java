package com.example.chat.channel.repository;

import com.example.chat.channel.entity.ChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessages, Long> {
    List<ChatMessages> findAllByChannelId(Long channelId);
}

package com.example.chat.channel.repository;

import com.example.chat.channel.domain.ChatMessages;
import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.entity.Chats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessages, Long> {
    List<ChatMessages> findAllByChannelId(Long channelId);
}

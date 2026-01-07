package com.example.chat.channel.repository;

import com.example.chat.channel.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChannelId(Long channelId);

    // 이 아이디보다 작은 과거 메시지를 ID 내림차순으로 20개 가져와라
    Slice<ChatMessage> findByChannelIdAndIdLessThanOrderByIdDesc(Long channelId, Long chatMessageId, Pageable pageable);

    Slice<ChatMessage> findByChannelIdOrderByIdDesc(Long channelId, Pageable pageable);
}

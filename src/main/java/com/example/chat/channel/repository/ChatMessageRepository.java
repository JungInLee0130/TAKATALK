package com.example.chat.channel.repository;

import com.example.chat.channel.entity.ChatMessages;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessages, Long> {
    List<ChatMessages> findAllByChannelId(Long channelId);

    // 이 아이디보다 작은 과거 메시지를 ID 내림차순으로 20개 가져와라
    Slice<ChatMessages> findByChannelIdAndIdLessThanOrderByIdDesc(Long channelId, Long id, Pageable pageable);

    Slice<ChatMessages> findByChannelIdOrderByIdDesc(Long channelId, Pageable pageable);
}

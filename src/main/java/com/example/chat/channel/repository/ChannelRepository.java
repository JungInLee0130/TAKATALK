package com.example.chat.channel.repository;

import com.example.chat.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByGroupId(Long groupId);

    List<Channel> findByGroupIdAndCategoryIsNull(Long groupId);
}

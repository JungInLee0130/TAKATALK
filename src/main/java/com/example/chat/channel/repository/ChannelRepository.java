package com.example.chat.channel.repository;

import com.example.chat.channel.entity.Channels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channels, Long> {
    List<Channels> findByGroupId(Long groupId);
}

package com.example.chat.channel.repository;

import com.example.chat.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByGroupId(Long groupId);

    @Query("select distinct c " +
            "from Channel c " +
            "where c.group.id = :groupId " +
            "and c.category is null")
    List<Channel> findByGroupIdAndCategoryIsNull(Long groupId);
}

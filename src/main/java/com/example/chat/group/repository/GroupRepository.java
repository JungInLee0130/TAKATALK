package com.example.chat.group.repository;

import com.example.chat.group.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Groups, Long> {
    List<Groups> findAllBySiteUserId(Long siteUserId);
}

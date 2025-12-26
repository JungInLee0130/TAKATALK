package com.example.chat.group.repository;

import com.example.chat.group.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Groups, Long> {

    Optional<Groups> findByInviteCode(String inviteCode);
}

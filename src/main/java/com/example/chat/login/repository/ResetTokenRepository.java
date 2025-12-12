package com.example.chat.login.repository;

import com.example.chat.login.entity.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    Optional<ResetToken> findByUUID(String UUID);
    boolean existsByUUID(String UUID);
}

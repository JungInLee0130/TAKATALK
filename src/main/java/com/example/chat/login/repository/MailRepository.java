package com.example.chat.login.repository;

import com.example.chat.login.entity.MailUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailRepository extends JpaRepository<MailUser, Long> {
    Optional<MailUser> findByEmail(String email);

    boolean existsByEmail(String email);
}

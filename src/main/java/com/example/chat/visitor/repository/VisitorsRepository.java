package com.example.chat.visitor.repository;

import com.example.chat.visitor.entity.Visitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface VisitorsRepository extends JpaRepository<Visitors, Long> {
    /*@Query(value = "select *  from visitors v" +
            "inner join channels c" +
            "on v.channel_id = c.channel_id ", nativeQuery = true)*/
    // 결과가 없더라도 빈리스트 반환
    List<Visitors> findByGroupId(Long groupId);
}

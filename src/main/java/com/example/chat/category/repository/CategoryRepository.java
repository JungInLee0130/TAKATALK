package com.example.chat.category.repository;

import com.example.chat.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByGroupId(Long groupId);

    // groupId에 해당하는 모든 카테고리 가져오기(채널이 없는 카테고리 포함)
    @Query("select distinct c from Category c " +
            "left join fetch c.channels " +
            "where c.group.id = :groupId")
    List<Category> findAllByGroupIdWithChannels(@Param("groupId") Long groupId);

}

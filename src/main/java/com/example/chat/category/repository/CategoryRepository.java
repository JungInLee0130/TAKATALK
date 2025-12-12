package com.example.chat.category.repository;

import com.example.chat.category.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
    List<Categories> findByGroupId(Long groupId);
}

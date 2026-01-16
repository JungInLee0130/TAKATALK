package com.example.chat.category.service;

import com.example.chat.category.dto.*;
import com.example.chat.category.entity.Category;
import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.entity.Group;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.groupmember.domain.GroupRole;
import com.example.chat.groupmember.domain.annotation.RequireGroupRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }
    public List<CategoryResponse> getCategorizedChannels(Long groupId) {
        List<CategoryResponse> categoryResponses = categoryRepository.findAllByGroupIdWithChannels(groupId)
                .stream()
                .map(CategoryResponse::from)
                .toList();

        return categoryResponses;
    }
    // 카테고리 생성
    @RequireGroupRole(GroupRole.OWNER)
    @Transactional
    public CategoryCreateResponse createCategory(Long groupId, CateGoryCreateRequest request) {
        Group group = groupRepository.getReferenceById(groupId);    // service layer 순환참조 방지
        Category category = Category.create(request.getName(), request.getIsSecret(), group);
        Category savedCategory = categoryRepository.save(category);
        return CategoryCreateResponse.from(savedCategory);
    }
    // 카테고리 수정
    @RequireGroupRole(GroupRole.OWNER)
    @Transactional
    public CategoryEditResponse editCategory(Long groupId, Long categoryId, CategoryUpdateRequest request) {
        Category category = findById(categoryId);
        category.updateCategory(request.getName(), request.getIsSecret());
        return CategoryEditResponse.from(category);
    }
    // 카테고리 삭제
    @RequireGroupRole(GroupRole.OWNER)
    @Transactional
    public void deleteCategory(Long groupId, Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}

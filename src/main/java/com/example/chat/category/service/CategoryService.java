package com.example.chat.category.service;

import com.example.chat.category.entity.Category;
import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.category.dto.CateGoryCreateRequest;
import com.example.chat.category.dto.CategoryGetResponse;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.group.entity.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;


    public List<CategoryGetResponse> getCategories(Long groupId) {
        List<Category> categories = categoryRepository.findByGroupId(groupId);
        return categories.stream()
                .map(category -> new CategoryGetResponse(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Category createCategory(CateGoryCreateRequest request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Category category = Category.create(
                request.getCategoryName(),
                request.getIsSecret(),
                group
        );

        return categoryRepository.save(category);
    }
}

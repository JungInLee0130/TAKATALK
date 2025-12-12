package com.example.chat.category.service;

import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.category.dto.CateGoryCreateForm;
import com.example.chat.category.dto.CategoryGetResponse;
import com.example.chat.category.entity.Categories;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.GroupRepository;
import com.example.chat.group.Groups;
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
        List<Categories> categories = categoryRepository.findByGroupId(groupId);
        return categories.stream()
                .map(category -> new CategoryGetResponse(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createCategory(Long groupId, CateGoryCreateForm request) {
        Groups group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Categories category = Categories.builder()
                .name(request.getCategoryName())
                .isSecret(request.getIsSecret())
                .group(group)
                .build();

        categoryRepository.save(category);
    }
}

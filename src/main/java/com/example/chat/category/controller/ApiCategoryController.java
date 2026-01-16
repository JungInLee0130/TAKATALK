package com.example.chat.category.controller;

import com.example.chat.category.dto.CateGoryCreateRequest;
import com.example.chat.category.dto.CategoryCreateResponse;
import com.example.chat.category.dto.CategoryUpdateRequest;
import com.example.chat.category.dto.CategoryEditResponse;
import com.example.chat.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Category Api", description = "(그룹-카테고리)로 이어지는 카테고리 Api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group/{groupId}/category")
public class ApiCategoryController {
    private final CategoryService categoryService;
    
    @Operation(summary = "카테고리 생성", description = "채널 카테고리를 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<CategoryCreateResponse> createCategory(@PathVariable(name = "groupId") Long groupId,
                                                                 @Valid @RequestBody CateGoryCreateRequest request) {
        CategoryCreateResponse response = categoryService.createCategory(groupId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);    // list
    }

    @Operation(summary = "카테고리 수정", description = "채널 카테고리를 수정합니다.")
    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryEditResponse> editCategory(@PathVariable(name = "groupId") Long groupId,
                                                                 @PathVariable(name = "categoryId") Long categoryId, @RequestBody CategoryUpdateRequest request) {
        CategoryEditResponse response = categoryService.editCategory(groupId, categoryId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카테고리 삭제", description = "채널 카테고리를 삭제합니다.")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "groupId") Long groupId,
                                               @PathVariable(name = "categoryId") Long categoryId) {
        categoryService.deleteCategory(groupId, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

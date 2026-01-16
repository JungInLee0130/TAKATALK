package com.example.chat.category.controller;

import com.example.chat.category.dto.CategoryCreateResponse;
import com.example.chat.category.service.CategoryService;
import com.example.chat.category.dto.CateGoryCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group/{groupId}/category")
public class CategoryController {
    private final CategoryService categoryService;
    /*
    * 카테고리 생성
    * */
    @PostMapping(value = "/create", produces = "application/string;charset=UTF-8")
    public ResponseEntity<CategoryCreateResponse> createCategory(@PathVariable(name = "groupId") Long groupId, @RequestBody CateGoryCreateRequest request) {
        CategoryCreateResponse response = categoryService.createCategory(groupId, request);
        // STATUS : 201, HEADER : location : URI.create
        return ResponseEntity.created(URI.create("/group/access/" + response.getGroupId())).build();
    }
}

package com.example.chat.category.controller;

import com.example.chat.category.entity.Category;
import com.example.chat.category.service.CategoryService;
import com.example.chat.category.dto.CateGoryCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    /*
    * 카테고리 생성
    * */
    /*@PostMapping(value = "/create/{groupId}", produces = "application/string;charset=UTF-8")
    public String createCategory(@PathVariable(name = "groupId") Long groupId,
                                 @ModelAttribute(name = "createForm") CateGoryCreateForm createForm,
                                 RedirectAttributes redirectAttributes) {
        categoryService.createCategory(groupId, createForm);
        redirectAttributes.addAttribute("groupId", groupId);
        return "redirect:/group/access/{groupId}";
    }*/

    @PostMapping(value = "/create", produces = "application/string;charset=UTF-8")
    public ResponseEntity<Void> createCategory(@RequestBody CateGoryCreateRequest request) {
        Category category = categoryService.createCategory(request);
        // STATUS : 201, HEADER : location : URI.create
        // 그냥 새로고침.
        return ResponseEntity.created(URI.create("/group/access/" + category.getGroup().getId())).build();
    }

    @GetMapping
    public String getCategories(Long groupId) {
        categoryService.getCategories(groupId);
        return "category";
    }
}

package com.example.chat.category.controller;

import com.example.chat.category.service.CategoryService;
import com.example.chat.category.dto.CateGoryCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    /*
    * 카테고리 생성
    * */
    @PostMapping("/create/{groupId}")
    public String createCategory(@PathVariable(name = "groupId") Long groupId,
                                 @ModelAttribute(name = "createForm") CateGoryCreateForm createForm,
                                 RedirectAttributes redirectAttributes) {
        categoryService.createCategory(groupId, createForm);
        redirectAttributes.addAttribute("groupId", groupId);
        return "redirect:/group/access/{groupId}";
    }
    @GetMapping
    public String getCategories(Long groupId) {
        categoryService.getCategories(groupId);
        return "category";
    }
}

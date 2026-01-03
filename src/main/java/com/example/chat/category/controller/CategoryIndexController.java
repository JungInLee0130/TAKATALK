package com.example.chat.category.controller;

import com.example.chat.category.dto.CateGoryCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryIndexController {

    @GetMapping("/create/{groupId}")
    public String createCategoryPage(@ModelAttribute(name = "createForm") CateGoryCreateRequest createForm,
                                     @PathVariable(name = "groupId") Long groupId,
                                     Model model) {
        model.addAttribute("groupId", groupId);
        return "category/category-create";
    }
}

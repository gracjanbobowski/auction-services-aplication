package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Category; // Popraw import
import com.example.auctionservicesaplication.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {

    private final CategoryService categoryService;
    // Konstruktor z wstrzykiwaniem zależności.
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    // Pobiera formularz do dodawania nowej kategorii.
    @GetMapping("/categories/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "add-category";
    }
    // Obsługuje proces dodawania nowej kategorii.
    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute Category category, Model model) {
        categoryService.addCategory(category);
        model.addAttribute("successMessage", "Category added successfully!");
        return "add-category";
    }
}
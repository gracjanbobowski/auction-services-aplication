package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// Controller that manages category-related operations.
@Controller
public class CategoryController {

    private final CategoryService categoryService;

    // Constructor with dependency injection for the CategoryService.
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Retrieves and displays the form for adding a new category.
    @GetMapping("/categories/add")
    public String showAddCategoryForm(Model model) {
        // Bind a new Category object to the form for capturing input.
        model.addAttribute("category", new Category());
        return "add-category";
    }

    // Handles the submission of the form for adding a new category.
    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute Category category, Model model) {
        // Calls the service to add the new category to the database.
        categoryService.addCategory(category);
        // Adds a success message to the model to notify the user.
        model.addAttribute("successMessage", "Category added successfully!");
        return "add-category";
    }
}
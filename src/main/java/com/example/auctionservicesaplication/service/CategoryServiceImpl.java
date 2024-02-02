package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of the CategoryService interface.
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // Constructor for dependency injection of the CategoryRepository.
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Retrieves all categories from the repository.
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Adds a new category to the repository.
    @Override
    public void addCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        categoryRepository.save(category);
    }
}
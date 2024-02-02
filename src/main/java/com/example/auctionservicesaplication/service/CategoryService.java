package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.model.Category;

import java.util.List;

// Service interface for managing categories.
public interface CategoryService {
    List<Category> getAllCategories();
    void addCategory(Category category);
}
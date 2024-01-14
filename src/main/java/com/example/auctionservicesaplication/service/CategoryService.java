package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.model.Category; // Popraw import

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void addCategory(Category category);
}

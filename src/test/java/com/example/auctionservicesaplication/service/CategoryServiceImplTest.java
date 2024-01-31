package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
<<<<<<< HEAD
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
=======
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
>>>>>>> origin/master

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

<<<<<<< HEAD
    @Test
    // Test 1: getAllCategories() - Pobiera wszystkie kategorie.
=======
    // Test 1: getAllCategories() - Retrieves all categories.
    @Test
>>>>>>> origin/master
    public void getAllCategories_retrievesAllCategories() {
        // Arrange
        List<Category> expectedCategories = new ArrayList<>();
        expectedCategories.add(new Category(1L, null, "Art", "Art category"));
        expectedCategories.add(new Category(2L, null, "Collectibles", "Collectibles category"));
        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = categoryService.getAllCategories();

        // Assert
        assertEquals(expectedCategories, actualCategories, "Should return all categories from the database.");
        verify(categoryRepository).findAll(); // Verifies that findAll was called
    }

<<<<<<< HEAD
    @Test
    // Test 2: addCategory() - Zapisuje nową kategorię.
=======
    // Test 2: addCategory() - Saves a new category.
    @Test
>>>>>>> origin/master
    public void addCategory_savesNewCategory() {
        // Arrange
        Category newCategory = new Category(null, null, "Electronics", "Gadgets and electronic devices.");
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        // Act
        categoryService.addCategory(newCategory);

        // Assert
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        Category savedCategory = categoryArgumentCaptor.getValue();

        assertNotNull(savedCategory);
        assertEquals("Electronics", savedCategory.getCategoryName());
    }

<<<<<<< HEAD
    @Test
    // Test 4: getAllCategories() when no categories - Zwraca pustą listę, gdy brak kategorii.
=======
    // Test 3: addCategory() with null Category - Throws Exception.
    @Test
    public void addCategory_withNullCategory_throwsException() {
        // Arrange
        Category nullCategory = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> categoryService.addCategory(nullCategory),
                "Should throw IllegalArgumentException when trying to save a null category.");
    }

    // Test 4: getAllCategories() when no categories - Returns empty list.
    @Test
>>>>>>> origin/master
    public void getAllCategories_whenNoCategories_returnsEmptyList() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Category> actualCategories = categoryService.getAllCategories();

        // Assert
        assertTrue(actualCategories.isEmpty(), "Should return an empty list when there are no categories.");
        verify(categoryRepository).findAll(); // Verifies that findAll was called
    }
<<<<<<< HEAD
}
=======

}
>>>>>>> origin/master

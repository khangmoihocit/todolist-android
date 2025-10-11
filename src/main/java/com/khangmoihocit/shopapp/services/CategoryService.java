package com.khangmoihocit.shopapp.services;

import com.khangmoihocit.shopapp.dtos.CategoryDTO;
import com.khangmoihocit.shopapp.models.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDTO category);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, CategoryDTO category);
    void deleteCategory(long id);
}

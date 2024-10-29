package com.dreamshops.service.category;

import com.dreamshops.entity.Category;

import java.util.List;

public interface CategoryService {

    Category getCategoryById(Long categoryId);

    Category getCategoryByName(String categoryName);

    List<Category> getAllCategory();

    Category addCategory(Category category);

    Category updateCategory(Category category, Long categoryId);

    void deleteCategory(String categoryId);

}

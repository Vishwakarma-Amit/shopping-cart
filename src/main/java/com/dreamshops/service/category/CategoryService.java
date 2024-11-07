package com.dreamshops.service.category;

import com.dreamshops.entity.Category;
import com.dreamshops.request.CategoryRequest;

import java.util.List;

public interface CategoryService {

    Category getCategoryById(int categoryId);

    Category getCategoryByName(String categoryName);

    List<Category> getAllCategory();

    Category addCategory(CategoryRequest request);

    Category updateCategory(Category category, int categoryId);

    void deleteCategory(int categoryId);
}

package com.dreamshops.service.category;

import com.dreamshops.entity.Category;
import com.dreamshops.exception.AlreadyExistsException;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CategoryRepository;
import com.dreamshops.request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id: "+categoryId));
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(CategoryRequest request) {
        boolean exists = categoryRepository.existsByName(request.getName());
        if(exists){
            throw new AlreadyExistsException("Category already exits with the name: "+request.getName());
        }
        return categoryRepository.save(new Category(request.getName()));
    }

    @Override
    public Category updateCategory(Category category, int categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id: "+categoryId));

        savedCategory.setName(category.getName()!=null && category.getName().isEmpty() ? category.getName() : savedCategory.getName());

        return categoryRepository.save(savedCategory);
    }

    @Override
    public void deleteCategory(int categoryId) {
        categoryRepository.findById(categoryId)
                .ifPresentOrElse(categoryRepository::delete,
                        ()-> {throw new ResourceNotFoundException("Category not found with id: "+categoryId);});
    }
}

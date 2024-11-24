package com.dreamshops.service.category;

import com.dreamshops.entity.Category;
import com.dreamshops.exception.AlreadyExistsException;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CategoryRepository;
import com.dreamshops.request.CategoryRequest;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(int categoryId) {
        final String methodName = "getCategoryById";
         Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException(Message.CATEGORY_NOT_FOUND +categoryId));
         log.info("{} - category found with id - {}", methodName, categoryId);
         return category;
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        final String methodName = "getCategoryByName";
        Category category = categoryRepository.findByName(categoryName);
        log.info("{} - category found by name - {}", methodName, categoryName);
        return category;
    }

    @Override
    public List<Category> getAllCategory() {
        final String methodName = "getAllCategory";
        List<Category> categories =categoryRepository.findAll();
        log.info("{} - category list fetched!", methodName);
        return categories;
    }

    @Override
    public Category addCategory(CategoryRequest request) {
        final String methodName = "addCategory";
        boolean exists = categoryRepository.existsByName(request.getName());
        if(exists){
            throw new AlreadyExistsException(Message.CATEGORY_ALREADY_EXISTS+request.getName());
        }
        Category category = categoryRepository.save(new Category(request.getName()));
        log.info("{} - category saved successfully!", methodName);
        return category;
    }

    @Override
    public Category updateCategory(Category category, int categoryId) {
        final String methodName = "updateCategory";
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException(Message.CATEGORY_NOT_FOUND+categoryId));
        log.info("{} - category retrieved by category id - {}", methodName, categoryId);

        savedCategory.setName(category.getName()!=null && category.getName().isEmpty() ? category.getName() : savedCategory.getName());
        log.info("{} - category updated successfully!", methodName);
        return categoryRepository.save(savedCategory);
    }

    @Override
    public void deleteCategory(int categoryId) {
        final String methodName = "deleteCategory";
        categoryRepository.findById(categoryId)
                .ifPresentOrElse(categoryRepository::delete,
                        ()-> {throw new ResourceNotFoundException(Message.CATEGORY_NOT_FOUND+categoryId);});

        log.info("{} - category by id - {} deleted!", methodName, categoryId);
    }
}

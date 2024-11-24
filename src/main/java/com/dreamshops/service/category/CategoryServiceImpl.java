package com.dreamshops.service.category;

import com.dreamshops.entity.Category;
import com.dreamshops.exception.AlreadyExistsException;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CategoryRepository;
import com.dreamshops.request.CategoryRequest;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException(Message.CATEGORY_NOT_FOUND +categoryId));
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
            throw new AlreadyExistsException(Message.CATEGORY_ALREADY_EXISTS+request.getName());
        }
        return categoryRepository.save(new Category(request.getName()));
    }

    @Override
    public Category updateCategory(Category category, int categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException(Message.CATEGORY_NOT_FOUND+categoryId));

        savedCategory.setName(category.getName()!=null && category.getName().isEmpty() ? category.getName() : savedCategory.getName());

        return categoryRepository.save(savedCategory);
    }

    @Override
    public void deleteCategory(int categoryId) {
        categoryRepository.findById(categoryId)
                .ifPresentOrElse(categoryRepository::delete,
                        ()-> {throw new ResourceNotFoundException(Message.CATEGORY_NOT_FOUND+categoryId);});
    }
}

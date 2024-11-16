package com.dreamshops.service.product;

import com.dreamshops.entity.Category;
import com.dreamshops.entity.Product;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CategoryRepository;
import com.dreamshops.repository.ProductRepository;
import com.dreamshops.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product addProduct(ProductRequest request) {
        // check if the category is found in the DB
        // If Yes, set it as the new product category
        // If No, the save it as a new category
        // Then set as the new product category.
        Category category = categoryRepository.findByName(request.getCategory().getName());
        if(category==null){
            category = categoryRepository.save(new Category(request.getCategory().getName()));
        }
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(ProductRequest request, Category category) {
        System.out.println(request);
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(int productId) {
        return productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with id - "+productId));
    }

    @Override
    public void deleteProductById(int productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(productRepository::delete,
                        ()->{throw new ResourceNotFoundException("Product not found with id - "+productId);});
    }

    @Override
    public Product updateProduct(ProductRequest request, int productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id - " + productId));

        existingProduct.setName(request.getName()!=null && request.getName().isEmpty() ? request.getName() : existingProduct.getName());
        existingProduct.setBrand(request.getBrand()!=null && request.getBrand().isEmpty()? request.getBrand() : existingProduct.getBrand());
        existingProduct.setInventory(request.getInventory()>=0 ? request.getInventory() : existingProduct.getInventory());
        existingProduct.setPrice(request.getPrice().compareTo(new BigDecimal(0))>=0?request.getPrice():existingProduct.getPrice());
        existingProduct.setDescription(request.getDescription()!=null && request.getDescription().isEmpty()? request.getDescription() : existingProduct.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(Objects.requireNonNullElseGet(category, () -> new Category(request.getCategory().getName())));
        return productRepository.save(existingProduct);
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String categoryName, String brand) {
        return productRepository.findByCategoryNameAndBrand(categoryName, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}

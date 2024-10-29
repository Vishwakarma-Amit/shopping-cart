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
    public Product add(ProductRequest request) {
        // Check if category is found in DB
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(ProductRequest productRequest, Category category){

        return new Product(
                productRequest.getName(),
                productRequest.getBrand(),
                productRequest.getPrice(),
                productRequest.getInventory(),
                productRequest.getDescription(),
                productRequest.getCategory()
        );
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with id - "+productId));
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(productRepository::delete,
                        ()->{throw new ResourceNotFoundException("Product not found with id - "+productId);});
    }

    @Override
    public Product updateProduct(ProductRequest request, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id - " + productId));

        existingProduct.setName(request.getName()!=null && !request.getName().isEmpty() ? request.getName() : existingProduct.getName());
        existingProduct.setBrand(request.getBrand()!=null && !request.getBrand().isEmpty()? request.getBrand() : existingProduct.getBrand());
        existingProduct.setInventory(request.getInventory()>=0 ? request.getInventory() : existingProduct.getInventory());
        existingProduct.setPrice(request.getPrice().compareTo(new BigDecimal(0))>=0? request.getPrice() : existingProduct.getPrice());
        existingProduct.setDescription(request.getDescription()!=null && !request.getDescription().isEmpty()? request.getDescription() : existingProduct.getDescription());

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

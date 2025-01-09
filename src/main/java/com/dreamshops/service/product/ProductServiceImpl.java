package com.dreamshops.service.product;

import com.dreamshops.dto.ProductDto;
import com.dreamshops.entity.Category;
import com.dreamshops.entity.Product;
import com.dreamshops.exception.AlreadyExistsException;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CategoryRepository;
import com.dreamshops.repository.ProductRepository;
import com.dreamshops.request.ProductRequest;
import com.dreamshops.utility.Message;
import com.dreamshops.utility.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final Converter productConverter;

    @Override
    public ProductDto addProduct(ProductRequest request) {
        final String methodName = "addProduct";
        // check if the category is found in the DB
        // If Yes, set it as the new product category
        // If No, the save it as a new category
        // Then set as the new product category.

        Product productExists = productExist(request.getName(), request.getBrand());
        if(productExists!=null){
            throw new AlreadyExistsException(Message.PRODUCT_ALREADY_EXISTS+productExists.getProductId());
        }

        Category category = categoryRepository.findByName(request.getCategory().getName());
        if(category==null){
            category = categoryRepository.save(new Category(request.getCategory().getName()));
        }
        log.info("{} - category id - {}", methodName, category.getCategoryId());

        Product product = productRepository.save(createProduct(request, category));
        log.info("{} - product persisted in db successfully - {}", methodName, product.getProductId());
        return productConverter.convertToDto(product);
    }

    private Product productExist(String name, String brand) {
        List<Product> products = productRepository.findByBrandAndName(brand, name);
        log.info("add Product :: product already exists, please update the product!");

        if(products!=null && !products.isEmpty()){
            return Objects.requireNonNull(products).get(0);
        }
        return null;
    }

    private Product createProduct(ProductRequest request, Category category) {
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
    public ProductDto getProductById(int productId) {
        final String methodName = "getProductById";
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id - " + productId));

        log.info("{} - product retrieved from db with product id - {}", methodName, product.getProductId());

        return productConverter.convertToDto(product);
    }

    @Override
    public void deleteProductById(int productId) {
        final String methodName = "deleteProductById";
        productRepository.findById(productId)
                .ifPresentOrElse(productRepository::delete,
                        ()->{throw new ResourceNotFoundException(Message.PRODUCT_NOT_FOUND +productId);});

        log.info("{} - product with product id - {} deleted successfully", methodName, productId);
    }

    @Override
    public ProductDto updateProduct(ProductRequest request, int productId) {
        final String methodName = "updateProduct";
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Message.PRODUCT_NOT_FOUND + productId));

        log.info("{} - product found from db with product id - {}", methodName, productId);

        existingProduct.setName(request.getName()!=null && !request.getName().isEmpty() ? request.getName() : existingProduct.getName());
        existingProduct.setBrand(request.getBrand()!=null && !request.getBrand().isEmpty()? request.getBrand() : existingProduct.getBrand());
        existingProduct.setInventory(request.getInventory()>=0 ? request.getInventory()+existingProduct.getInventory() : existingProduct.getInventory());
        existingProduct.setPrice(request.getPrice().compareTo(new BigDecimal(0))>=0?request.getPrice():existingProduct.getPrice());
        existingProduct.setDescription(request.getDescription()!=null && !request.getDescription().isEmpty()? request.getDescription() : existingProduct.getDescription());

        log.info("{} - updated product details, product id - {}", methodName, productId);

        Category category = categoryRepository.findByName(request.getCategory().getName());

        existingProduct.setCategory(Objects.requireNonNullElseGet(category, () -> new Category(request.getCategory().getName())));
        log.info("{} - existing product updated, product id - {}", methodName, productId);

        return productConverter.convertToDto(productRepository.save(existingProduct));
    }


    @Override
    public List<ProductDto> getAllProducts() {
        final String methodName = "getAllProducts";
        List<Product> products = productRepository.findAll();
        log.info("{} - All product list fetched!", methodName);
        return productConverter.getConvertedProduct(products);
    }

    @Override
    public List<ProductDto> getProductsByCategory(String categoryName) {
        final String methodName = "getProductsByCategory";
        List<Product> products = productRepository.findByCategoryName(categoryName);
        log.info("{} - product list by category fetched, category - {}", methodName, categoryName);
        return productConverter.getConvertedProduct(products);
    }

    @Override
    public List<ProductDto> getProductsByBrand(String brand) {
        final String methodName = "getProductsByBrand";
        List<Product> products =  productRepository.findByBrand(brand);
        log.info("{} - product list by brand fetched, brand - {}", methodName, brand);
        return productConverter.getConvertedProduct(products);
    }

    @Override
    public List<ProductDto> getProductsByCategoryAndBrand(String categoryName, String brand) {
        final String methodName = "getProductsByCategoryAndBrand";
        List<Product> products =  productRepository.findByCategoryNameAndBrand(categoryName, brand);
        log.info("{} - product list, brand - {}, category - {}", methodName, brand, categoryName);
        return productConverter.getConvertedProduct(products);
    }

    @Override
    public List<ProductDto> getProductsByName(String name) {
        final String methodName = "getProductsByName";
        List<Product> products =  productRepository.findByName(name);
        log.info("{} - product list by name - {}", methodName, name);
        return productConverter.getConvertedProduct(products);
    }

    @Override
    public List<ProductDto> getProductsByBrandAndName(String brand, String name) {
        final String methodName = "getProductsByBrandAndName";
        List<Product> products =  productRepository.findByBrandAndName(brand, name);
        log.info("{} - product list by brand and name, brand - {}, name - {}", methodName, brand, name);
        return productConverter.getConvertedProduct(products);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        final String methodName = "countProductsByBrandAndName";
        long count = productRepository.countByBrandAndName(brand, name);
        log.info("{} - product counted, brand - {}, name - {}", methodName,brand, name);
        return count;
    }

}

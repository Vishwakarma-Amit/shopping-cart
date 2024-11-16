package com.dreamshops.service.product;

import com.dreamshops.dto.ImageDto;
import com.dreamshops.dto.ProductDto;
import com.dreamshops.entity.Category;
import com.dreamshops.entity.Image;
import com.dreamshops.entity.Product;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.CategoryRepository;
import com.dreamshops.repository.ImageRepository;
import com.dreamshops.repository.ProductRepository;
import com.dreamshops.request.ProductRequest;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Product addProduct(ProductRequest request) {
        // check if the category is found in the DB
        // If Yes, set it as the new product category
        // If No, the save it as a new category
        // Then set as the new product category.
        Category category = categoryRepository.findByName(request.getCategory().getName());
        System.out.println(category);
        if(category==null){
            category = categoryRepository.save(new Category(request.getCategory().getName()));
        }
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
    public ProductDto getProductById(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id - " + productId));
        System.out.println(product);
        return convertToDto(product);
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
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return getConvertedProduct(products);
    }

    @Override
    public List<ProductDto> getProductsByCategory(String categoryName) {
        List<Product> products = productRepository.findByCategoryName(categoryName);
        return getConvertedProduct(products);
    }

    @Override
    public List<ProductDto> getProductsByBrand(String brand) {
        List<Product> products =  productRepository.findByBrand(brand);
        return getConvertedProduct(products);
    }

    @Override
    public List<ProductDto> getProductsByCategoryAndBrand(String categoryName, String brand) {
        List<Product> products =  productRepository.findByCategoryNameAndBrand(categoryName, brand);
        return getConvertedProduct(products);
    }

    @Override
    public List<ProductDto> getProductsByName(String name) {
        List<Product> products =  productRepository.findByName(name);
        return getConvertedProduct(products);
    }

    @Override
    public List<ProductDto> getProductsByBrandAndName(String brand, String name) {
        List<Product> products =  productRepository.findByBrandAndName(brand, name);
        return getConvertedProduct(products);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    public List<ProductDto> getConvertedProduct(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductProductId(product.getProductId());
        List<ImageDto> imageDtos = images.stream().map(image->modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}

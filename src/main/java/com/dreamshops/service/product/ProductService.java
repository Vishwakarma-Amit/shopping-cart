package com.dreamshops.service.product;

import java.util.List;

import com.dreamshops.entity.Product;

public interface ProductService {
	
	Product add (Product product);
	
	Product getProductById(Long productId);
	
	void deleteProductById(String productId);
	
	Product updateProduct(Product product, Long productId);
	
	List<Product> getAllProducts();
	
	List<Product> getProductsByCategory(Long categoryId);
	
	List<Product> getProductsByBrand(String brand);
	
	List<Product> getProductsByCategoryAndBrand(Long categoryId, String brand);
	
	List<Product> getProductsByName(String name);
	
	List<Product> getProductsByBrandAndName(String brand, String name);
	
	Long countProductsByBrandAndName(String brand);

}

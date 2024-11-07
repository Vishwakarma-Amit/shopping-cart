package com.dreamshops.service.product;

import java.util.List;

import com.dreamshops.entity.Product;
import com.dreamshops.request.ProductRequest;

public interface ProductService {
	
	Product addProduct (ProductRequest request);
	
	Product getProductById(int productId);
	
	void deleteProductById(int productId);

	Product updateProduct(ProductRequest request, int productId);

	List<Product> getAllProducts();
	
	List<Product> getProductsByCategory(String categoryName);
	
	List<Product> getProductsByBrand(String brand);
	
	List<Product> getProductsByCategoryAndBrand(String categoryName, String brand);
	
	List<Product> getProductsByName(String name);
	
	List<Product> getProductsByBrandAndName(String brand, String name);
	
	Long countProductsByBrandAndName(String brand, String name);

}

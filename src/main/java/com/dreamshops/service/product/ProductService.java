package com.dreamshops.service.product;

import java.util.List;

import com.dreamshops.dto.ProductDto;
import com.dreamshops.request.ProductRequest;

public interface ProductService {
	
	ProductDto addProduct (ProductRequest request);
	
	ProductDto getProductById(int productId);
	
	void deleteProductById(int productId);

	ProductDto updateProduct(ProductRequest request, int productId);

	List<ProductDto> getAllProducts();
	
	List<ProductDto> getProductsByCategory(String categoryName);
	
	List<ProductDto> getProductsByBrand(String brand);
	
	List<ProductDto> getProductsByCategoryAndBrand(String categoryName, String brand);
	
	List<ProductDto> getProductsByName(String name);
	
	List<ProductDto> getProductsByBrandAndName(String brand, String name);
	
	Long countProductsByBrandAndName(String brand, String name);

}

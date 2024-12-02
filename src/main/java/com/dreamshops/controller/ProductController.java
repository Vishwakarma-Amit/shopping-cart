package com.dreamshops.controller;

import com.dreamshops.dto.ProductDto;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.request.ProductRequest;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.service.product.ProductService;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<ProductDto> allProducts = productService.getAllProducts();
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, allProducts), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable int productId) {
        try {
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, productService.getProductById(productId)), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductRequest productRequest){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, productService.addProduct(productRequest)), HttpStatus.CREATED);
        }catch (Exception ex){
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String categoryName) {
        try {
            List<ProductDto> productsByCategory = productService.getProductsByCategory(categoryName);
            if(productsByCategory.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND, null), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, productsByCategory), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable int productId){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_SUCCESSFUL, productService.updateProduct(productRequest, productId)), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED, ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex){
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable int productId) {
        try {
            productService.deleteProductById(productId);
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_SUCCESSFUL, null), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED, ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/brand/{brandName}")
    public ResponseEntity<ApiResponse> getProductsByBrand(@PathVariable String brandName) {
        try {
            List<ProductDto> productsByBrand = productService.getProductsByBrand(brandName);
            if(productsByBrand.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND, null), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, productsByBrand), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bac/{brandName}/{categoryName}")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@PathVariable String brandName, @PathVariable String categoryName) {
        try {
            List<ProductDto> productsByCategoryAndBrand = productService.getProductsByCategoryAndBrand(categoryName, brandName);
            if(productsByCategoryAndBrand.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND, null), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, productsByCategoryAndBrand), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name/{productName}")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String productName) {
        try {
            List<ProductDto> productsByName = productService.getProductsByName(productName);
            if(productsByName.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND, null), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, productsByName), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ban/{brandName}/{productName}")
    public ResponseEntity<ApiResponse> getProductsByNameAndBrand(@PathVariable String brandName, @PathVariable String productName) {
        try {
            List<ProductDto> productsByBrandAndName = productService.getProductsByBrandAndName(brandName, productName);
            if(productsByBrandAndName.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND, null), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, productsByBrandAndName), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count/{brandName}/{productName}")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@PathVariable String brandName, @PathVariable String productName) {
        try {
            Long count = productService.countProductsByBrandAndName(brandName, productName);
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, count), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
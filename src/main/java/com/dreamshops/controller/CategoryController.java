package com.dreamshops.controller;

import com.dreamshops.entity.Category;
import com.dreamshops.exception.AlreadyExistsException;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.request.CategoryRequest;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.service.category.CategoryService;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories(){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS,categoryService.getAllCategory()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable int categoryId){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS,categoryService.getCategoryById(categoryId)), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.NOT_FOUND);
        }catch (Exception ex){
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String categoryName){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS,categoryService.getCategoryByName(categoryName)), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.NOT_FOUND);
        }catch (Exception ex){
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryRequest categoryRequest){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS,categoryService.addCategory(categoryRequest)), HttpStatus.CREATED);
        }catch (AlreadyExistsException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.CONFLICT);
        }catch (Exception ex){
            return new ResponseEntity<>(new ApiResponse(Message.FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable int categoryId){
        try{
            categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_SUCCESSFUL,null), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED,ex.getMessage()), HttpStatus.NOT_FOUND);
        }catch (Exception ex){
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category, @PathVariable int categoryId){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_SUCCESSFUL,categoryService.updateCategory(category, categoryId)), HttpStatus.OK);
        }catch (AlreadyExistsException ex) {
            return new ResponseEntity<>(new ApiResponse(Message.UPLOAD_FAILED,ex.getMessage()), HttpStatus.CONFLICT);
        }catch (Exception ex){
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

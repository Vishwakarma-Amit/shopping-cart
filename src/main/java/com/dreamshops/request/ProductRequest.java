package com.dreamshops.request;

import com.dreamshops.entity.Category;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductRequest{

    private int productId;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Brand is required")
    @Size(max = 50, message = "Brand cannot exceed 50 characters")
    private String brand;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @Min(value = 0, message = "Inventory cannot be negative")
    private int inventory;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Category is required")
    private Category category;
}

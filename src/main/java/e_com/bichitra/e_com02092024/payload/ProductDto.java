package e_com.bichitra.e_com02092024.payload;

import e_com.bichitra.e_com02092024.model.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long productId;
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String productName;
    private String image;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;
    @DecimalMin(value = "0.0", message = "Discount must be at least 0")
    @DecimalMax(value = "100.0", message = "Discount cannot be more than 100%")
    private double discount;
    private double specialPrice;
    private  Category category;

}
package e_com.bichitra.e_com02092024.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long categoryId;
    @NotBlank
    @Size(min = 3, message = "Category name must contain least 3 characters")
    private String categoryName;

//    public CategoryDto() {
//        this.categoryId = UUID.randomUUID().toString();
//    }
}

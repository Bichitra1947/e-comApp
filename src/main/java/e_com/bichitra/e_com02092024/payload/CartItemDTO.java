package e_com.bichitra.e_com02092024.payload;

import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private CartDTO cart;
    private ProductDto productDTO;
    private Integer quantity;
    @Digits(integer = 10, fraction = 2)
    private Double price;
    private Double discount;
    @Digits(integer = 10, fraction = 2)
    private Double specialPrice;
}

package e_com.bichitra.e_com02092024.payload;

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
    private Double discount;
    private Double productPrice;
    private Double discountPrice;
}

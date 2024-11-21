package e_com.bichitra.e_com02092024.service;

import e_com.bichitra.e_com02092024.payload.CartDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long product_id,Integer quantity);
    CartDTO getCartById(String email,Long cart_id);
    List<CartDTO> getCarts();
    @Transactional
    CartDTO updateProductQuantityInCart(Long product_id, Integer quantity);
    @Transactional
    String deleteProductFromCart(Long cart_id, Long product_id);

    void updateProductInCarts(Long cartId, Long productId);
}

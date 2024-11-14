package e_com.bichitra.e_com02092024.controller;

import e_com.bichitra.e_com02092024.model.Cart;
import e_com.bichitra.e_com02092024.payload.CartDTO;
import e_com.bichitra.e_com02092024.repository.CartRepository;
import e_com.bichitra.e_com02092024.service.CartService;
import e_com.bichitra.e_com02092024.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartRepository cartRepository;
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){

        final CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return ResponseEntity.ok(cartDTO);
    }


    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
        final String email = authUtil.loggedInEmail();
        final Cart cart = cartRepository.findCartByEmail(email);
        final Long cartId = cart.getCartId();
        final CartDTO cartDTO = cartService.getCartById(email, cartId);
        return ResponseEntity.ok(cartDTO) ;
    }
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        final List<CartDTO> cartDTOList = cartService.getCarts();
        return ResponseEntity.ok(cartDTOList);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable String operation){
        final CartDTO cartDTO = cartService
                .updateProductQuantityInCart(productId, operation.equalsIgnoreCase("delete") ? -1 : 1);
        return ResponseEntity.ok(cartDTO);
    }
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId){
        final String delete = cartService.deleteProductFromCart(cartId, productId);
        return ResponseEntity.ok(delete);
    }
}

package e_com.bichitra.e_com02092024.repository;

import e_com.bichitra.e_com02092024.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

//    findByCartItemIdAndProduct_id(Long cartItemId,Long product_id);
	@Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
//	@Query("SELECT c FROM CartItem c WHERE c.cartItemId = :cartItemId AND c.product = :productId")
	CartItem findCartItemByCartIdAndProductId(Long cartId, Long product_id);

//	@Modifying
//	@Query("DELETE FROM CartItem ci WHERE ci.product.productId AND ci.cart.cartId")
//	void deleteCartItemByProductIdAndCartId(Long product_id,Long cart_id);

	@Modifying
	@Query("DELETE FROM CartItem ci WHERE ci.product.productId = :productId AND ci.cart.cartId = :cartId")
	void deleteCartItemByProductIdAndCartId(@Param("productId") Long productId, @Param("cartId") Long cartId);
}

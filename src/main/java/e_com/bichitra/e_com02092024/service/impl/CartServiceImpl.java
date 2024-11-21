package e_com.bichitra.e_com02092024.service.impl;

import e_com.bichitra.e_com02092024.exception.APIException;
import e_com.bichitra.e_com02092024.exception.ResourceNotFoundException;
import e_com.bichitra.e_com02092024.model.Cart;
import e_com.bichitra.e_com02092024.model.CartItem;
import e_com.bichitra.e_com02092024.model.Product;
import e_com.bichitra.e_com02092024.payload.CartDTO;
import e_com.bichitra.e_com02092024.payload.CartItemDTO;
import e_com.bichitra.e_com02092024.payload.ProductDto;
import e_com.bichitra.e_com02092024.repository.CartItemRepository;
import e_com.bichitra.e_com02092024.repository.CartRepository;
import e_com.bichitra.e_com02092024.repository.ProductRepository;
import e_com.bichitra.e_com02092024.service.CartService;
import e_com.bichitra.e_com02092024.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;

//    @Override
//    public CartDTO addProductToCart(Long product_id, Integer quantity) {
//        Cart cart = createCart();
//        final Product product = productRepository.findById(product_id)
//                .orElseThrow(() -> new ResourceNotFoundException("product", "product_id", product_id));
//        final CartItem cartItemFromDB = cartItemRepository.
//                findCartItemByCartIdAndProductId(cart.getCartId(), product.getProductId());
//
//        final List<CartItem> cartItems = cart.getCartItems();
//        if(cartItemFromDB==null){
//            cart.setTotalPrice(0.0);
//        }
////        var totalPrice1=0.0;
////        for(CartItem cartItem:cartItems) {
////            cartItem.setSpecialPrice(cartItem.getSpecialPrice() * cartItem.getQuantity());
////            totalPrice1 = totalPrice1 + cartItem.getSpecialPrice();
////        }
////        cart.setTotalPrice(totalPrice1);
//        cartRepository.save(cart);
//        if (cartItemFromDB != null) {
//            throw new APIException("Product " + product.getProductName() + " is already Exist");
//        }
//        if (product.getQuantity() == 0) {
//            throw new APIException("Product " + product.getProductName() + " not Available");
//        }
//        if (product.getQuantity() < quantity) {
//            throw new APIException("Please, make an order of the " + product.getProductName()
//                    + " less than or equal to the quantity " + product.getQuantity() + ".");
//        }
//        CartItem newCartItem=new CartItem();
//        newCartItem.setProduct(product);
//        newCartItem.setCart(cart);
//        newCartItem.setPrice(product.getPrice());
//        newCartItem.setDiscount(product.getDiscount());
//        newCartItem.setQuantity(quantity);
//        final CartItem saveCartItem = cartItemRepository.save(newCartItem);
//
//        saveCartItem.setSpecialPrice(saveCartItem.getPrice()-(saveCartItem.getPrice()*saveCartItem.getDiscount()/100));
//        cartItemRepository.save(saveCartItem);
//
//        final var totalPrice = cart.getTotalPrice() + (saveCartItem.getSpecialPrice()*saveCartItem.getQuantity());
//        cart.setTotalPrice(totalPrice);
//        final Cart saveCart = cartRepository.save(cart);
//
//        CartDTO cartDTO = modelMapper.map(saveCart, CartDTO.class);
//        cartDTO.setTotalPrice(totalPrice);
//        final List<ProductDto> productDtoList = cartItems.stream().map(item -> {
//            ProductDto productDto = modelMapper.map(item.getProduct(), ProductDto.class);
//            productDto.setQuantity(item.getQuantity());
//            return productDto;
//        }).toList();
//        cartDTO.setProducts(productDtoList);
//        return cartDTO;
//    }
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(cart.getCartId(), productId);
//        if(cartItem==null){
//            cart.setTotalPrice(0.0);
//            cartRepository.save(cart);
//        }

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        final Cart cartFromDB = cartRepository.findById(cart.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", cart.getCartId()));

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cartFromDB);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setPrice(product.getPrice());
        final CartItem saveCartItem = cartItemRepository.save(newCartItem);

        saveCartItem.setSpecialPrice(saveCartItem.getPrice()-(saveCartItem.getPrice()*saveCartItem.getDiscount()/100));
        final var saveCartItem2 = cartItemRepository.save(saveCartItem);

        product.setQuantity(product.getQuantity());


        final var totalPrice = cartFromDB.getTotalPrice();

        cartFromDB.setTotalPrice(totalPrice +
                (saveCartItem2.getSpecialPrice() * saveCartItem2.getQuantity()));

        final Cart cartFromDB2 = cartRepository.save(cartFromDB);
        CartDTO cartDTO = modelMapper.map(cartFromDB2, CartDTO.class);
        List<CartItem> cartItems = cartFromDB2.getCartItems();

        final List<ProductDto> productDtoList = cartItems.stream().map(item -> {
            ProductDto map = modelMapper.map(item.getProduct(), ProductDto.class);
            map.setQuantity(item.getQuantity());
            return map;
        }).toList();
        cartDTO.setProducts(productDtoList);
        return cartDTO;
    }
    @Override
    public CartDTO getCartById(String email,Long cart_id){
        final Cart cart = cartRepository.findCartByEmailAndCartId(email, cart_id);
        if(cart==null){
            throw new ResourceNotFoundException("Cart"+"cart_id"+cart_id);
        }
        final List<CartItem> cartItemList = cartItemRepository.findAll();
        cartItemList.forEach(c ->
                c.getProduct().setQuantity(c.getQuantity()));
        var totalPrice=0.0;
        for(CartItem cartItem:cartItemList) {
            cartItem.setPrice(cartItem.getPrice() * cartItem.getQuantity());
            totalPrice =totalPrice+ cartItem.getSpecialPrice();
        }
        final CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setTotalPrice(totalPrice);
        final List<ProductDto> productDtoList = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDto.class))
                .toList();
        cartDTO.setProducts(productDtoList);
        return cartDTO;
    }

    @Override
    public List<CartDTO> getCarts() {
        final Cart cart1 = createCart();
        final List<Cart> cartList = cartRepository.findAll();
        if(cartList.isEmpty()){
            throw new APIException("No cart exists");
        }
        var specialPrice=0.0;
        final List<CartItem> cartItems = cartItemRepository.findAll();
        for(CartItem cartItem:cartItems){
            specialPrice = specialPrice+cartItem.getSpecialPrice();
            cart1.setTotalPrice(specialPrice*cartItem.getQuantity());
        }
        cartRepository.save(cart1);
        List<CartDTO> cartDTOList = cartList.stream().map(cart -> {
            return modelMapper.map(cart, CartDTO.class);
        }).toList();
        return cartDTOList;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long product_id, Integer quantity){
        final String email = authUtil.loggedInEmail();
        final Cart cartFromEmail = cartRepository.findCartByEmail(email);
        final Cart cart = cartRepository.findById(cartFromEmail.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cart_id", cartFromEmail.getCartId()));
        final Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "product_id", product_id));
        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }
        if(product.getQuantity()==0){
            throw new APIException(product.getProductId()+" is not available");
        }
        final CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(cart.getCartId(), product_id);
        if(cartItem==null){
            throw  new APIException("Product "+product.getProductName()+" is not exist inside the card");
        }
        final int newQuantity = cartItem.getQuantity() + quantity;
        cartItem.setQuantity(newQuantity);
        if(newQuantity<0){
            throw new APIException("Product is not available");
        }
        Double discountPrice=product.getPrice()-(product.getPrice()*(product.getDiscount()/100));
        if(newQuantity==0){
            deleteProductFromCart(cart.getCartId(), product_id);
        }else {
            cartItem.setProduct(product);
            cartItem.setQuantity(newQuantity);
            cartItem.setPrice(product.getPrice());
            cartItem.setDiscount(discountPrice);
            cart.setTotalPrice(cart.getTotalPrice()+(cartItem.getPrice()*quantity));
        }
        final CartItem updateCartItem = cartItemRepository.save(cartItem);
        final CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        final List<CartItem> cartItems = cart.getCartItems();
        final CartItemDTO cartItemDTO = modelMapper.map(updateCartItem, CartItemDTO.class);
        final List<ProductDto> productDtoList = cartItems.stream()
                .map(cartItem1 ->{
                    final ProductDto productDto = modelMapper.map(cartItem1.getProduct(), ProductDto.class);
                    productDto.setQuantity(updateCartItem.getQuantity());
                    productDto.setPrice(updateCartItem.getQuantity()*updateCartItem.getPrice());
                    double specialPrice=
                            updateCartItem.getPrice()-(updateCartItem.getPrice()*(updateCartItem.getDiscount()/100));
                    productDto.setSpecialPrice(updateCartItem.getQuantity()*updateCartItem.getSpecialPrice());
                    return productDto;
                })
                .toList();
        cartDTO.setProducts(productDtoList);
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cart_id,Long product_id){
        final Cart cart = cartRepository.findById(cart_id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cart_id", cart_id));
        productRepository.findById(product_id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "product_id", product_id));
        final CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(cart_id, product_id);
        if(cartItem==null){
            throw new APIException("Cart item is not available");
        }
        cartItemRepository.deleteCartItemByProductIdAndCartId(product_id,cart_id);

        return "Product "+cartItem.getProduct().getProductName()+" is removed from cart";
    }
    @Override
    public void updateProductInCarts(Long cartId, Long productId){
        final Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
        final CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(cartId, productId);
        if(cartItem==null){
            throw new APIException("Product "+product.getProductId()+" is not exist inside the cart");
        }
        double cartPrice = cart.getTotalPrice()
                - (cartItem.getSpecialPrice() * cartItem.getQuantity());
        cartItem.setSpecialPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice
                + (cartItem.getSpecialPrice() * cartItem.getQuantity()));
        cartRepository.save(cart);
        cartItemRepository.save(cartItem);
    }
    public Cart createCart() {
        final Cart cartExist = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (cartExist != null)
            return cartExist;

        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUsername());
        final Cart newCart = cartRepository.save(cart);
        return newCart;

    }


}


















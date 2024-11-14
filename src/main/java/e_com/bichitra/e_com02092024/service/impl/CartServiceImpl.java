package e_com.bichitra.e_com02092024.service.impl;

import e_com.bichitra.e_com02092024.exception.APIException;
import e_com.bichitra.e_com02092024.exception.ResourceNotFoundException;
import e_com.bichitra.e_com02092024.model.Cart;
import e_com.bichitra.e_com02092024.model.CartItem;
import e_com.bichitra.e_com02092024.model.Product;
import e_com.bichitra.e_com02092024.payload.CartDTO;
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
import java.util.stream.Collectors;
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

    @Override
    public CartDTO addProductToCart(Long product_id, Integer quantity) {

        Cart cart = createCart();
        final Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new ResourceNotFoundException("product", "product_id", product_id));

        final CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(cart.getCartId(), product.getProductId());
        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " is already Exist");
        }
        if (product.getQuantity() == 0) {
            throw new APIException("Product " + product.getProductName() + " not Available");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setPrice(product.getPrice());
        newCartItem.setDiscountPrice(product.getDiscount());
        newCartItem.setQuantity(quantity);
        final CartItem save = cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        final List<CartItem> cartItems = cart.getCartItems();

//        cartItems.stream()
//                .map(cartItem1 -> new ProductDto(cartItem1.getProduct().getProductId(), cartItem1.getProduct().getPrice()))
//                .collect(Collectors.toList());

//        final List<ProductDto> collect = cartItems
//                .stream()
//                .map(item -> modelMapper.map(cartItems, ProductDto.class))
//                .collect(Collectors.toList());
//        cartDTO.setProducts(collect);

        Stream<ProductDto> productStream = cartItems.stream().map(item -> {
            ProductDto map = modelMapper.map(item.getProduct(), ProductDto.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }

    @Override
    public CartDTO getCartById(String email,Long cart_id){
        final Cart cart = cartRepository.findCartByEmailAndCartId(email, cart_id);
        if(cart==null){
            throw new ResourceNotFoundException("Cart"+"cart_id"+cart_id);
        }
        final CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        final List<CartItem> itemList = cart.getCartItems();
        for(CartItem cartItem:itemList){
            final var product = cartItem.getProduct();
            product.setQuantity(cartItem.getQuantity());
        }
        final List<ProductDto> productDtoList = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDto.class))
                .toList();

        cartDTO.setProducts(productDtoList);
        return cartDTO;
    }

    @Override
    public List<CartDTO> getCarts() {
        final List<Cart> cartList = cartRepository.findAll();
//        final Cart cartFromAuth = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(cartList.isEmpty()){
            throw new APIException("No cart exists");
        }
//        final CartDTO cartDTO = modelMapper.map(cartFromAuth, CartDTO.class);
//        final List<CartDTO> cartDTOList = cartList.stream()
//                .map(cart -> modelMapper.map(cart, CartDTO.class)).toList();
//        final List<ProductDto> productDtoList = cartFromAuth.getCartItems().stream()
//                .map(cartItem -> modelMapper.map(cartItem.getProduct(), ProductDto.class)).toList();
//        cartDTO.setProducts(productDtoList);
        List<CartDTO> cartDTOList = cartList.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

//            final  List<CartItem> cartItems = cart.getCartItems();
//            cartItems.stream().forEach(cartItem -> cartItem.getProduct().setQuantity(cartItem.getQuantity()));
//            List<ProductDto> products = cart.getCartItems().stream()
//                    .map(p -> modelMapper.map(p.getProduct(), ProductDto.class)).collect(Collectors.toList());
//            cartDTO.setProducts(products);
            return cartDTO;

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
            cartItem.setDiscountPrice(discountPrice);
            cart.setTotalPrice(cart.getTotalPrice()+(cartItem.getPrice()*quantity));
        }
        final CartItem updateCartItem = cartItemRepository.save(cartItem);
        final CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        final List<CartItem> cartItems = cart.getCartItems();

        final List<ProductDto> productDtoList = cartItems.stream()
                .map(cartItem1 ->{
                    final ProductDto productDto = modelMapper.map(cartItem1.getProduct(), ProductDto.class);
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

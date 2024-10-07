package e_com.bichitra.e_com02092024.controller;

import e_com.bichitra.e_com02092024.apiResponse.ProductResponse;
import e_com.bichitra.e_com02092024.payload.ProductDto;
import e_com.bichitra.e_com02092024.service.ProductService;
import e_com.bichitra.e_com02092024.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto, @PathVariable Long categoryId){
        final var serviceProductDto = productService.createProduct(productDto,categoryId);
        return new ResponseEntity<>(serviceProductDto, HttpStatus.OK);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProduct(
            @RequestParam(name = "pageNumber",defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "PageSize",defaultValue = "15") Integer PageSize,
            @RequestParam(name = "sortProductByField",defaultValue = AppConstants.SORT_PRODUCT_NAME) String sortProductByField,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrderBy
    ){
        final var productResponse = productService.getAllProduct(pageNumber,PageSize,sortProductByField,sortOrderBy );
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId){
        final ProductResponse productResponse = productService.getProductByCategoryId(categoryId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber",defaultValue = "0") Integer pageNumber,
                                                                @RequestParam(name = "PageSize",defaultValue = "15") Integer PageSize,
                                                                @RequestParam(name = "sortProductByField",defaultValue = AppConstants.SORT_PRODUCT_NAME) String sortProductByField,
                                                                @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrderBy){
        ProductResponse productResponse=productService.searchProductByKeyword(keyword,pageNumber,PageSize,sortProductByField,sortOrderBy);
        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
    }
    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId,@Valid @RequestBody ProductDto productDto){
        ProductDto productDtoService =productService.updatesExitingProduct(productId,productDto);
        return new ResponseEntity<>(productDtoService,HttpStatus.OK);
    }
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long productId){
        ProductDto productDto=productService.removeProduct(productId);
        return new ResponseEntity<>(productDto,HttpStatus.ACCEPTED);
    }
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDto> updateProductImage
            (@PathVariable Long productId , @RequestParam MultipartFile image) throws IOException {
        ProductDto productDto=productService.updateProductImageFile(productId,image);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
}

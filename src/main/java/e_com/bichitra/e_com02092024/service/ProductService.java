package e_com.bichitra.e_com02092024.service;

import e_com.bichitra.e_com02092024.apiResponse.ProductResponse;
import e_com.bichitra.e_com02092024.model.Product;
import e_com.bichitra.e_com02092024.payload.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto,Long categoryId);
    ProductResponse getAllProduct(Integer pageNumber,Integer pageSize,String sortByField,String sortByOrder);

    ProductResponse getProductByCategoryId(Long categoryId);

    ProductResponse searchProductByKeyword(String keyword,Integer pageNumber, Integer pageSize,
                                           String sortByField, String sortByOrder);

    ProductDto updatesExitingProduct(Long productId, ProductDto productDto);

    ProductDto removeProduct(Long productId);

    ProductDto updateProductImageFile(Long productId, MultipartFile image) throws IOException;
}

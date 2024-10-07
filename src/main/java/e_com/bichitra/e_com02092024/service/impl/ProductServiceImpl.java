package e_com.bichitra.e_com02092024.service.impl;

import e_com.bichitra.e_com02092024.apiResponse.ProductResponse;
import e_com.bichitra.e_com02092024.exception.APIException;
import e_com.bichitra.e_com02092024.exception.ResourceNotFoundException;
import e_com.bichitra.e_com02092024.model.Category;
import e_com.bichitra.e_com02092024.model.Product;
import e_com.bichitra.e_com02092024.payload.ProductDto;
import e_com.bichitra.e_com02092024.repository.CategoryRepository;
import e_com.bichitra.e_com02092024.repository.ProductRepository;
import e_com.bichitra.e_com02092024.service.ImageUpdateService;
import e_com.bichitra.e_com02092024.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ImageUpdateService imageUpdateService;
    @Value("${project.image}")
    String filePath ;
    @Override
    public ProductDto createProduct(ProductDto productDto, Long categoryId) {
        var categoryFromDB = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId ", categoryId));

        //check product is there or not
//        Product productFromDB=productRepository.findByProductNameIgnoreCase
//                        (product.getProductName());
//        if(productFromDB!=null){
//            throw new APIException(productFromDB.getProductName()+" product already exist");
//        }

        List<Product> products = categoryFromDB.getProduct();
        boolean isProductExist=true;
        for(Product val:products){
            if (val.getProductName().equalsIgnoreCase(productDto.getProductName())) {
                isProductExist = false;
                break;
            }
        }
        if(isProductExist) {
            Product product = modelMapper.map(productDto, Product.class);
            product.setCategory(categoryFromDB);
            product.setImage("default.png");
            double specialPrice = productDto.getPrice() - (productDto.getPrice() * (productDto.getDiscount() / 100));
            product.setSpecialPrice(specialPrice);
            final var saveProduct = productRepository.save(product);
            return modelMapper.map(saveProduct, ProductDto.class);
        }else {
            throw new APIException(" product already exist");
        }

    }

    public ProductResponse getAllProduct(
            Integer pageNumber,Integer pageSize,
            String sortByField,String sortByOrder
    ){
        final Sort sort = sortByOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortByField).ascending()
                : Sort.by(sortByField).descending();
        final PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,sort);
        final Page<Product> productPage = productRepository.findAll(pageRequest);
        final List<Product> products = productPage.getContent();

        if(products.isEmpty()){
            throw new APIException("No category found.");
        }
        final List<ProductDto> productDtoList = products.stream().map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDtoList);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategoryId(Long categoryId) {
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));

        final List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        final List<ProductDto> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        ProductResponse response=new ProductResponse();
        response.setContent(productDTOs);
        return  response;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize,
                                                  String sortByField, String sortByOrder) {

//        final var productDtoList = products.stream()
//                .map(productDto -> modelMapper.map(productDto, ProductDto.class))
//                .collect(Collectors.toList());

        final Sort sort = sortByOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortByField).ascending()
                : Sort.by(sortByField).descending();
        final PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> productPage=productRepository.findByProductNameContainingIgnoreCase(keyword,pageRequest);
        final var productsContent = productPage.getContent();
        final var productDTOs = productsContent.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

        if(productPage.isEmpty()){
            throw new APIException("No category found.");
        }

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductDto updatesExitingProduct(Long productId, ProductDto productDto) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));

        Product productFromDB=productRepository.findByProductNameIgnoreCase(productDto.getProductName());
        if(productFromDB!=null){
            throw new APIException(productFromDB.getProductName()+" product already exist");
        }

        productFromDB.setProductName(productDto.getProductName());
        productFromDB.setDescription(productDto.getDescription());
        productFromDB.setPrice(productDto.getPrice());
        productFromDB.setDiscount(productDto.getDiscount());
        double specialPrice = productDto.getPrice() - (productDto.getPrice() * (productDto.getDiscount() / 100));
        productFromDB.setSpecialPrice(specialPrice);
        final var saveProduct = productRepository.save(productFromDB);
        return modelMapper.map(saveProduct, ProductDto.class);
    }

    @Override
    public ProductDto removeProduct(Long productId) {
        final var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));
        productRepository.delete(product);
        return modelMapper.map(product,ProductDto.class);
    }

    @Override
    public ProductDto updateProductImageFile(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));

        final String fileName = imageUpdateService.uploadImage(filePath, image);

        productFromDB.setImage(fileName);
        final var saveProduct = productRepository.save(productFromDB);

        return modelMapper.map(saveProduct, ProductDto.class);
    }



}

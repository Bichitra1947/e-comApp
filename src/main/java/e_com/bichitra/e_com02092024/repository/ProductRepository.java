package e_com.bichitra.e_com02092024.repository;

import e_com.bichitra.e_com02092024.model.Category;
import e_com.bichitra.e_com02092024.model.Product;
import e_com.bichitra.e_com02092024.payload.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);

    Page<Product> findByProductNameContainingIgnoreCase(String keyword, PageRequest pageRequest );

    Product findByProductNameIgnoreCase(String productName);
}

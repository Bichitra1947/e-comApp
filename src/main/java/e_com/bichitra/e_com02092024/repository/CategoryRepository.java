package e_com.bichitra.e_com02092024.repository;

import e_com.bichitra.e_com02092024.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByCategoryName(String name);

    Optional<Category> findById(Long id);
}

package e_com.bichitra.e_com02092024.repository;

import e_com.bichitra.e_com02092024.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUserName(String username);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);
}

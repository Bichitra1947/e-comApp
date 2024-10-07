package e_com.bichitra.e_com02092024.repository;

import e_com.bichitra.e_com02092024.constant.AppRole;
import e_com.bichitra.e_com02092024.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles,Integer> {
    Optional<Roles> findByRoleName(AppRole appRole);
}

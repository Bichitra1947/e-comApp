package e_com.bichitra.e_com02092024.repository;

import e_com.bichitra.e_com02092024.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    @Query(value = "SELECT * FROM addresses WHERE user_id = ?",nativeQuery = true)
	public List<Address> findByUserId(Long id);
    
}

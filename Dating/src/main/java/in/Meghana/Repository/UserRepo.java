package in.Meghana.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.Meghana.Entity.User;
import jakarta.transaction.Transactional;
@Transactional
public interface UserRepo extends JpaRepository<User, Integer>{

	Optional<User> findById(Long userId);
    
	@Query("select u from User u where u.email=:email and u.password=:password")
	User findByEmailAndPassword(String email,String password);
	
	 User findOneUserById(Long id);
	
}

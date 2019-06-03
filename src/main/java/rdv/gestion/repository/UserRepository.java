package rdv.gestion.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import rdv.gestion.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	User findByIdentifiant(String identifiant);
	@Query("select x from User x where x.identifiant=?1 and x.password=?2")
	User findUser(String identifiant, String password);
}
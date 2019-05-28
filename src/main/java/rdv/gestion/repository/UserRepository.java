package rdv.gestion.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import rdv.gestion.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	User findByIdentifiant(String identifiant);
	@Query("select droits from User where identifiant=?1 and password=?2")
	Integer getDroitsFromUser(String identifiant, String password);
}
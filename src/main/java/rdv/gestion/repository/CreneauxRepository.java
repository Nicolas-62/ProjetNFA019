package rdv.gestion.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import rdv.gestion.model.Creneaux;

public interface CreneauxRepository extends CrudRepository<Creneaux, Integer>{
	/*
	 * you have to mark it @Transactional as well as @Modifying, 
	 * which instruct Spring that it can modify existing records.
	 */
	@Transactional
	@Modifying
	@Query("delete from Creneaux c where c.medecin.id=?1")
	void deleteByIdMedecin(Integer id);
	

}

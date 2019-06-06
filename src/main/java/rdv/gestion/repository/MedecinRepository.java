package rdv.gestion.repository;

import org.springframework.data.repository.CrudRepository;

import rdv.gestion.model.Medecin;

public interface MedecinRepository extends CrudRepository<Medecin, Integer>{
	
	Iterable<Medecin> findByNom(String nom);
	Medecin findByUser_id(Integer user_id);

}

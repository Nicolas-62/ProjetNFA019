package rdv.gestion.repository;

import rdv.gestion.model.Medecin;
import org.springframework.data.repository.CrudRepository;

public interface MedecinRepository extends CrudRepository<Medecin, Integer>{
	
	Iterable<Medecin> findByNom(String nom);

}

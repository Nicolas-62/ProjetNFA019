package rdv.gestion.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import rdv.gestion.model.Patient;

public interface PatientRepository extends CrudRepository<Patient, Integer>{
	Patient findByNumSecu(String numSecu);
	Patient findByUser_id(Integer user_id);
	
	// recherche tout les patients qui ont rendez vous avec un médecin donné
	@Query("select distinct p from Patient p, Rv r, Creneaux c"
			+ " where p.id=r.patient.id and c.id=r.creneaux.id and c.medecin.id=?1")
	Iterable<Patient> findByRvMedecin(Integer medecin_id);
	
}

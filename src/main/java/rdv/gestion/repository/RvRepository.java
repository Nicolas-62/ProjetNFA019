package rdv.gestion.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import rdv.gestion.model.Rv;

public interface RvRepository extends CrudRepository<Rv, Integer>{
	Iterable<Rv> findByPatient_id(Integer Patient_id);
	
	/*
	 * Retourne pour un médecin donné, la liste des rendez-vous qu'il a avec un patient donné
	 */
	@Query("select r from Rv r, Creneaux c "
			+ "where r.creneaux.id=c.id "
			+ "and r.patient.id=?1 and c.medecin.id=?2")
	Iterable<Rv>findByPatientMedecin(Integer Patient_id, Integer Medecin_id);
}

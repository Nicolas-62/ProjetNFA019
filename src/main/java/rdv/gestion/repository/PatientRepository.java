package rdv.gestion.repository;

import org.springframework.data.repository.CrudRepository;

import rdv.gestion.model.Patient;
import rdv.gestion.model.User;

public interface PatientRepository extends CrudRepository<Patient, Integer>{
	Patient findByNumSecu(String numSecu);
	Patient findByUser_id(Integer user_id);
}

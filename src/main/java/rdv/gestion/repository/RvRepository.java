package rdv.gestion.repository;

import org.springframework.data.repository.CrudRepository;

import rdv.gestion.model.Rv;

public interface RvRepository extends CrudRepository<Rv, Integer>{
	Iterable<Rv> findByPatient_id(Integer Patient_id);
}

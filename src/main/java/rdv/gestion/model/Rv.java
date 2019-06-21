package rdv.gestion.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        name="rv", 
        uniqueConstraints=
            @UniqueConstraint(name="date_creneaux", columnNames={"date", "creneaux_id"})
    )
public class Rv {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="date")
	@NotNull
	private Date date;
	
	// un rendez vous correspond à un creneau donné
	@ManyToOne
	private Creneaux creneaux;
	
	// un rendez vous correspond à un patient donné	
	@ManyToOne
	private Patient patient;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Creneaux getCreneaux() {
		return creneaux;
	}

	public void setCreneaux(Creneaux creneaux) {
		this.creneaux = creneaux;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
		
}

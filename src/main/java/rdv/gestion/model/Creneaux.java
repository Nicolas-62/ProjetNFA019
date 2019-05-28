package rdv.gestion.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Creneaux {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	private Integer hDebut;
	@NotNull
	private Integer mDebut;
	@NotNull
	private Integer hFin;
	@NotNull
	private Integer mFin;	
	// un créneau est associé à un medecin et un médecin possède plusieurs créneaux
	@ManyToOne
	private Medecin medecin;
	// un creneau peut appartenir à plusieurs rendez vous de date (jour) differente
	@OneToMany(mappedBy="creneaux")
	private Collection<Rv> rv;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer gethDebut() {
		return hDebut;
	}
	public void sethDebut(Integer hDebut) {
		this.hDebut = hDebut;
	}
	public Integer getmDebut() {
		return mDebut;
	}
	public void setmDebut(Integer mDebut) {
		this.mDebut = mDebut;
	}
	public Integer gethFin() {
		return hFin;
	}
	public void sethFin(Integer hFin) {
		this.hFin = hFin;
	}
	public Integer getmFin() {
		return mFin;
	}
	public void setmFin(Integer mFin) {
		this.mFin = mFin;
	}
	public Medecin getMedecin() {
		return medecin;
	}
	public void setMedecin(Medecin medecin) {
		this.medecin = medecin;
	}
	public Collection<Rv> getRv() {
		return rv;
	}
	public void setRv(Collection<Rv> rv) {
		this.rv = rv;
	}
	
}

package rdv.gestion.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(
        name="patient", 
        uniqueConstraints=
            @UniqueConstraint(name="numSecu", columnNames={"numSecu"})
    )
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	@Pattern(regexp = "^([0-9]{13})$", message = "Format saisie invalide")
	private String numSecu;
	@NotNull
	@Pattern(regexp = "(M\\.)?(Mme)?", message = "format invalide")
	private String civilite;	
	@NotNull
	@Pattern(regexp = "^[a-zA-Zàâéèëêïîôùüçœ\\'’ -]{2,25}$", message = "Format du nom invalide")
	private String nom;
	@NotNull
	@Size(max = 30)
	@Pattern(regexp = "^[a-zA-Zàâéèëêïîôùüçœ\\'’ -]{2,25}$", message = "Format du nom invalide")
	private String prenom;
	@NotNull
	@Pattern(regexp = "^0[1-9]([-\\. ]?[0-9]{2}){4}$", message = "Format saisie invalide")
	private String tel;
	@Email
	@NotNull
	@Pattern(regexp = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", message = "Format de mail invalide")
	private String mail;
	@NotNull
	@Pattern(regexp = "^[a-zA-Z0-9àâéèëêïîôùüçœ\\'’ \\._ ,]{5,60}$", message = "Format saisie invalide")
	private String adresse;
	@NotNull
	@Pattern(regexp = "^[a-zA-Zàâéèëêïîôùüçœ\\'’ -]{2,25}$", message = "Format saisie invalide")
	private String ville;
	@NotNull
	@Pattern(regexp = "^([0-9]{5})$", message = "Format saisie invalide")
	private String cp;
	@NotNull
	private char sexe;
	// un patient possède plusieurs rendez vous
	@OneToMany(mappedBy="patient")
	private Collection<Rv> rv;
	// Un patient possède un compte utilisateur
	@NotNull
	@OneToOne
	private User user;	
	
	@Override
	public String toString() {
		return String.format("Patient [nom=%s, prenom=%s, user=%s]", nom, prenom, user);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public char getSexe() {
		return sexe;
	}
	public void setSexe(char sexe) {
		this.sexe = sexe;
	}
	public Collection<Rv> getRv() {
		return rv;
	}
	public void setRv(Collection<Rv> rv) {
		this.rv = rv;
	}
	public String getNumSecu() {
		return numSecu;
	}
	public void setNumSecu(String numSecu) {
		this.numSecu = numSecu;
	}
	public String getCivilite() {
		return civilite;
	}
	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}

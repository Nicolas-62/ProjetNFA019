package rdv.gestion.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Medecin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	@Size(max = 25)
	@Pattern(regexp = "^[a-zA-Zàâéèëêïîôùüçœ\\'’ -]{2,25}$", message = "Format saisie invalide")
	private String nom;
	@NotNull
	@Size(max = 25)
	@Pattern(regexp = "^[a-zA-Zàâéèëêïîôùüçœ\\'’ -]{2,25}$", message = "Format saisie invalide")
	private String prenom;
	@NotNull
	@Size(max = 50)
	private String specialite;
	@NotNull
	@Size(max = 20)
	@Pattern(regexp = "^0[1-9]([-\\. ]?[0-9]{2}){4}$", message = "Format saisie invalide")
	private String tel;
	@NotNull
	@Email
	@Size(max = 50)
	@Pattern(regexp = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", message = "Format de mail invalide")
	private String mail;
	@NotNull
	@Size(max = 60)
	@Pattern(regexp = "^[a-zA-Z0-9àâéèëêïîôùüçœ\\'’ \\._ ,]{5,60}$", message = "Format saisie invalide")
	private String adresse;
	@NotNull
	@Size(max = 25)
	@Pattern(regexp = "^[a-zA-Zàâéèëêïîôùüçœ\\'’ -]{2,25}$", message = "Format saisie invalide")
	private String ville;
	@NotNull
	@Size(max = 5)
	@Pattern(regexp = "^([0-9]{5})$", message = "Format saisie invalide")
	private String cp;
	// un medecin possède plusieurs créneaux
	@OneToMany(mappedBy="medecin")
	private Collection<Creneaux> creneaux;
	// Un medecin possède un compte utilisateur
	@NotNull
	@OneToOne
	private User user;
	
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

	public String getSpecialite() {
		return specialite;
	}

	public void setSpecialite(String specialite) {
		this.specialite = specialite;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Collection<Creneaux> getCreneaux() {
		return creneaux;
	}

	public void setCreneaux(Collection<Creneaux> creneaux) {
		this.creneaux = creneaux;
	}

}

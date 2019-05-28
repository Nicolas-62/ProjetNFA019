package rdv.gestion.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(
        name="user", 
        uniqueConstraints=
            @UniqueConstraint(name="identifiant", columnNames={"identifiant"})
    )
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	@Size(min = 5, max = 30, message = "Taille entre {min} et {max}")	
	private String identifiant;	
	/*SHA-3 is the latest secure hashing standard after SHA-2. Compared to SHA-2, SHA-3 provides 
	 * a different approach to generate a unique one-way hash, and it can be much faster on some 
	 * hardware implementations. Similar to SHA-256, SHA3-256 is the 256-bit fixed-length algorithm 
	 * in SHA-3.	
	 */
	@NotNull
	@Size(min = 5, max = 30, message = "Taille entre {min} et {max}")	
	private String password;

	@NotNull
//	@Pattern(regexp = "^[123]{1}$")	
	private Integer droits;

	@Override
	public String toString() {
		return String.format("User [id=%s, identifiant=%s, password=%s, droits=%s]", id, identifiant, password, droits);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getDroits() {
		return droits;
	}

	public void setDroits(Integer droits) {
		this.droits = droits;
	}

}

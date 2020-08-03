

package tn.esprit.pfe.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import utilities.BCrypt;

@Entity
@Table(name = "User", uniqueConstraints = { @UniqueConstraint(columnNames = "email", name = "uniqueEmailConstraint") })
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "USER_TYPE")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;

	@Column
	@NotBlank(message = "Donnez un nom de famille")
	private String nom;

	@Column
	@NotBlank(message = "Donnez un prenom")
	private String prenom;

	@Column(nullable = false)
	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\." + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
			+ "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Cet email est invalide")
	private String email;

	@Column
	private String password;

	@Length(min = 8, message = "Le mot de passe doit contenir un minimum de 8 caractères")
	@NotNull
	@Transient
	@JsonIgnore
	private String plainPassword="testtest";

	@Column
	private String role;
	

	@OneToMany(mappedBy="user")
	private List<Notifications> nootifications;

	public User() {
		super();
	}

	public User(String nom, String prenom, String email, String plainPassword) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.plainPassword = plainPassword;
	}
	
	

	public User(String nom, String prenom, String email, String password, String role) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String createPwd(String password) {
		String newPass;
		newPass = BCrypt.hashpw(password, BCrypt.gensalt());
		return newPass;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", password=" + password
				+ ", plainPassword=" + plainPassword + ", role=" + role + "]";
	}
	
	

}



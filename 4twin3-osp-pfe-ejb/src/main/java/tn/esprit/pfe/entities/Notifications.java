package tn.esprit.pfe.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;




@Entity
public class Notifications  implements Serializable{
	
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idNotification;
	
	private String text; 
	private String etat;
	
	@ManyToOne
	private User user;

	
	

	public Notifications() {
		super();
	}

	public int getIdNotification() {
		return idNotification;
	}

	public void setIdNotification(int idNotification) {
		this.idNotification = idNotification;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Notifications(String text, User user) {
		super();
		this.text = text;
		this.user = user;
	}

	public Notifications(int idNotification, String etat) {
		super();
		this.idNotification = idNotification;
		this.etat = etat;
	}


	
	
	
	
	
}

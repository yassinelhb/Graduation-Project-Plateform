package tn.esprit.pfe.interfaces;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.validation.ConstraintViolation;
import javax.xml.ws.Response;

import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.entities.User;
import utilities.ValidationError;

@Remote
public interface UserServiceRemote {
	public Set<ValidationError> addUser(User u);
	public User login(String email,String password);

}

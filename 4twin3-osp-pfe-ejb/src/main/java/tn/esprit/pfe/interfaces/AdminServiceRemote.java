package tn.esprit.pfe.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Admin;

@Remote
public interface AdminServiceRemote {
	public List<Admin> getListAdmin();
	public Admin getAdmin(int id);
}

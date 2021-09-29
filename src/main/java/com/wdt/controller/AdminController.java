package com.wdt.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.wdt.model.Administrateur;
import com.wdt.model.Role;
import com.wdt.repository.RoleRepository;
import com.wdt.utils.AdminService;
import com.wdt.utils.RoleService;

@Named
@ViewScoped
@Scope(scopeName = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AdminController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private List<Administrateur> admins;

	@Autowired
    private Administrateur selectedAdmin;

    private List<Administrateur> selectedAdmins;

	private Set<String> selectedRoles = new HashSet<String>();

	@Autowired
    private RoleRepository roleRepository;
	
    @Autowired
	@Named("adminService") 
    private AdminService adminService;
    
    @Autowired
	@Named("roleService") 
    private RoleService roleService;
    
    
    @Autowired	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    private Set<Administrateur> sAdmins=new HashSet<>();

	@PostConstruct
	public void init() {
    	this.admins = new ArrayList<>();
		this.adminService.getAdminRepository().findAll().forEach(admins::add);
	}

    /*************************************** Getters & Setters *********************************************************************/
    public Set<String> getSelectedRoles() {
    	for(Role r : selectedAdmin.getRoles()) {
    		selectedRoles.add(r.getRole());
    	}
  		return selectedRoles;
  	}

  	public void setSelectedRoles(Set<String> selectedRoles) {
  		this.selectedRoles = selectedRoles;
  	}
  	
    public List<Administrateur> getAdmins() {
    	this.admins = new ArrayList<>();
		this.adminService.getAdminRepository().findAll().forEach(admins::add);
		return admins;
    }
    
    public void setAdmins(List<Administrateur> admins) {
		this.admins = admins;
	}

    public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public Administrateur getSelectedAdmin() {
        return selectedAdmin;
    }

    public void setSelectedAdmin(Administrateur selectedAdmin) {
        this.selectedAdmin = selectedAdmin;
    }

    public List<Administrateur> getSelectedAdmins() {
        return selectedAdmins;
    }

    public void setSelectedAdmins(List<Administrateur> selectedAdmins) {
        this.selectedAdmins = selectedAdmins;
    }
    
    public RoleRepository getRoleRepository() {
		return roleRepository;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public Set<Administrateur> getsAdmins() {
		return sAdmins;
	}

	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setsAdmins(Set<Administrateur> sAdmins) {
		this.sAdmins = sAdmins;
	}
	
/************************************************************************************************************************************/
    public void openNew() {
        this.selectedAdmin = new Administrateur();
        //this.adminService.getAdminRepository().save(this.selectedAdmin);
		//this.init();
    }
    
    public void saveAdmin() {
    	
    	for(Administrateur a : this.selectedAdmins) {
    		this.sAdmins.add(a);
    	}
        if (this.selectedAdmin.getIdAdministrateur() == null) {
        	
        	for(String role : this.selectedRoles) {
        		
        		Role r = this.roleRepository.findByRole(role);
        		
        		//r.getAdministrateurs().add(this.selectedAdmin);
            	this.selectedAdmin.getRoles().add(r);
            	
            	//this.roleService.getRoleRepository().save(r);
            }
        	
        	this.adminService.getAdminRepository().save(new Administrateur(this.selectedAdmin.getNom(),
        			                                                       this.selectedAdmin.getPrenom(),
        			                                                       this.selectedAdmin.getEmail(),
        			                                                       bCryptPasswordEncoder.encode(this.selectedAdmin.getPassword()),
        			                                                       (byte)0,
        			                                                       this.selectedAdmin.getRoles()));
        	
    		this.selectedAdmin = new Administrateur();
    		this.selectedRoles = new HashSet<String>();
        	
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Administrateur Added"));
        }
        else {
            for (Role role : this.selectedAdmin.getRoles()) {
            	this.selectedRoles.add(role.getRole());
            }
          
            this.selectedAdmin.setPassword(bCryptPasswordEncoder.encode(this.selectedAdmin.getPassword()));
            
            this.adminService.getAdminRepository().save(this.selectedAdmin);
    		this.selectedAdmin = new Administrateur();
    		this.selectedRoles = new HashSet<String>();
    		
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Administrateur Updated"));
        }
        
		//this.init();

		PrimeFaces.current().executeScript("PF('manageadminDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-admins");
    }

    public void deleteAdmin() {
    	this.adminService.getAdminRepository().delete(selectedAdmin);
        this.admins.remove(this.selectedAdmin);
        this.selectedAdmin = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Admin Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-admins");

		this.init();
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedAdmins()) {
            int size = this.selectedAdmins.size();
            return size > 1 ? size + " Admins selected" : "1 Admin selected";
        }

        return "Delete";
    }

    public boolean hasSelectedAdmins() {
        return this.selectedAdmins != null && !this.selectedAdmins.isEmpty();
    }

    public void deleteSelectedAdmins() {
    	this.adminService.getAdminRepository().deleteAll(selectedAdmins);
        this.admins.removeAll(this.selectedAdmins);
        this.selectedAdmins = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Admins Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-admins");
        PrimeFaces.current().executeScript("PF('dtadmins').clearFilters()");
    }
    

}

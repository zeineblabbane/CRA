package com.wdt.model;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;


@Entity
@Table(name = "role", catalog = "wdt")
@Component
public class Role implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer idRole;
	private Set<Administrateur> administrateurs = new HashSet<Administrateur>();
	private String role;

	public Role() {
	}

	public Role(Set<Administrateur> administrateurs, String role) {
		this.administrateurs = administrateurs;
		this.role = role;
	}
	
	@Override
	public String toString() {
		return  role ;
	}
		
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idRole", unique = true, nullable = false)
	public Integer getIdRole() {
		return this.idRole;
	}

	public void setIdRole(Integer idRole) {
		this.idRole = idRole;
	}

	
	@ManyToMany( cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable( name = "Admins_Roles_Associations", joinColumns = @JoinColumn( name = "idRole" ), inverseJoinColumns = @JoinColumn( name = "idAdministrateur" ) )
	public Set<Administrateur> getAdministrateurs() {
		return this.administrateurs;
	}

	public void setAdministrateurs(Set<Administrateur> administrateurs) {
		this.administrateurs = administrateurs;
	}

	@Column(name = "role", nullable = false, length = 45)
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}


}

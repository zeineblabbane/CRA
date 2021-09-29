package com.wdt.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "administrateur", catalog = "wdt")
@Component
public class Administrateur implements java.io.Serializable {

	@Override
	public String toString() {
		return "Administrateur [idAdministrateur=" + idAdministrateur + ", nom=" + nom + ", prenom=" + prenom + "]";
	}

	private static final long serialVersionUID = 1L;
	
	private Integer idAdministrateur;
	private String nom;
	private String prenom;
	private String email;
	private String password;
	private byte enabled;
	private Set<Role> roles = new HashSet<Role>();
	private Set<Cra<?>> cra = new HashSet<Cra<?>>(0);

	public Administrateur() {
	}

	public Administrateur(String nom, String prenom, String email, String password, byte enabled) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
	}

	public Administrateur(String nom, String prenom, String email, String password, byte enabled, Set<Role> roles) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_administrateur", unique = true, nullable = false)
	public Integer getIdAdministrateur() {
		return this.idAdministrateur;
	}

	public void setIdAdministrateur(Integer idAdministrateur) {
		this.idAdministrateur = idAdministrateur;
	}

	@Column(name = "nom", nullable = false, length = 45)
	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Column(name = "prenom", nullable = false, length = 45)
	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	@Column(name = "email", nullable = false, length = 255)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "password", nullable = false, length = 155)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "enabled", nullable = false)
	public byte getEnabled() {
		return this.enabled;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}

	@ManyToMany( cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable( name = "Admins_Roles_Associations", joinColumns = @JoinColumn( name = "idAdministrateur" ), inverseJoinColumns = @JoinColumn( name = "idRole" ) )
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "administrateur", cascade  = CascadeType.MERGE)
	public Set<Cra<?>> getCra() {
		return this.cra;
	}

	public void setCra(Set<Cra<?>> cra) {
		this.cra = cra;
	}
	
	public Administrateur clone() {
    	return new Administrateur(getNom(), getPrenom(), getEmail(), getPassword(), getEnabled(), getRoles());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Administrateur other = (Administrateur) obj;
        if (nom == null) {
            return other.nom == null;
        } else return nom.equals(other.nom);
    }

}

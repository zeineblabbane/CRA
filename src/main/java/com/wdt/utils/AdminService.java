package com.wdt.utils;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wdt.model.Administrateur;
import com.wdt.model.Role;
import com.wdt.repository.AdminRepository;

@Named
@ApplicationScoped
@Service("adminService")
@Component
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    
    private Set < GrantedAuthority > grantedAuthorities = new HashSet<>();
  
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    	Administrateur user = adminRepository.findByNom(username);
    	
    	for(Role role : user.getRoles()) {
    		grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
    	}
    	System.out.println(user.getRoles());
    	
        return new org.springframework.security.core.userdetails.User(user.getNom(), user.getPassword(),grantedAuthorities);   
    }

    
    
	public AdminRepository getAdminRepository() {
		return adminRepository;
	}

	public void setAdminRepository(AdminRepository userRepository) {
		this.adminRepository = userRepository;
	}

	public Set<GrantedAuthority> getGrantedAuthorities() {
		return grantedAuthorities;
	}

	public void setGrantedAuthorities(Set<GrantedAuthority> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}
}
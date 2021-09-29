package com.wdt.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wdt.model.Role;
import com.wdt.repository.RoleRepository;

@Named
@ApplicationScoped
@Service("roleService")
public class RoleService {
    
	@Autowired
	private RoleRepository roleRepository;

	public RoleRepository getRoleRepository() {
		return roleRepository;
	}

	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	@Transactional
    public void addRole(Role role){
        roleRepository.save(role);
    }

}
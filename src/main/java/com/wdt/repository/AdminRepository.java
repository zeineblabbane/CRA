package com.wdt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wdt.model.Administrateur;

@Repository
public interface AdminRepository extends JpaRepository<Administrateur, Integer> {
    
	 Administrateur findByNom(String username);
	 Administrateur findByPassword(String password);
}

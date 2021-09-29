package com.wdt.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wdt.model.Cra;

@Repository
public interface CraRepository extends JpaRepository<Cra<?>,Integer>{
	
	Cra<?> findByStartDate(LocalDateTime startDate);
}
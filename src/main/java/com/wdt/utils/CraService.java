package com.wdt.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wdt.model.Cra;
import com.wdt.repository.CraRepository;


@Named
@ApplicationScoped
@Service("craService")
public class CraService {
	
	@Autowired
	private CraRepository craRepository;

	public CraRepository getCraRepository() {
		return craRepository;
	}

	public void setCraRepository(CraRepository craRepository) {
		this.craRepository = craRepository;
	}
	
	@Transactional
    public void addCra(Cra<?> cra){
        craRepository.save(cra);
    }

}







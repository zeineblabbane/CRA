package com.wdt;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import com.wdt.model.Administrateur;
//import com.wdt.repository.AdminRepository;


@SpringBootApplication
public class AuthenApplication /*implements CommandLineRunner*/ {
	
	
	public static void main(String[] args) {
		SpringApplication.run(AuthenApplication.class, args);
	}
	
	/*
	@Autowired	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
 	@Autowired
    private AdminRepository userRepository;

   @Override
    public void run(String...args) throws Exception {
        userRepository.save(new Administrateur("molka","molka","molka@gmail.com",bCryptPasswordEncoder.encode("molka"),(byte)0));
    }*/
}

package com.wdt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
	    return new MySimpleUrlAuthenticationSuccessHandler();
	}
	
	@Override
	  protected void configure(HttpSecurity http) throws Exception {
		
	    // require all requests to be authenticated except for the resources
	    http.authorizeRequests().antMatchers("/javax.faces.resource/**","/resources/**")
	        .permitAll().anyRequest().authenticated();
	    
	    // login
	    http.formLogin().loginPage("/login.xhtml")
	        .successHandler(myAuthenticationSuccessHandler())
	        .failureUrl("/login.xhtml?error=true")
	        .permitAll();
	    
	    // logout
	    http.logout().logoutSuccessUrl("/login.xhtml");
	    
	    // not needed as JSF 2.2 is implicitly protected against CSRF
	    http.csrf().disable();
	  }
	
	
	@Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

	  @Autowired
	  public void configureGlobal(AuthenticationManagerBuilder auth)
	      throws Exception {
		  
		  auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

	  }
}

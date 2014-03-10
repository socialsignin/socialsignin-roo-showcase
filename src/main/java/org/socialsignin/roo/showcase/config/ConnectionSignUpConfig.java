package org.socialsignin.roo.showcase.config;

import javax.annotation.PostConstruct;

import org.socialsignin.springsocial.security.signup.SpringSocialSecurityConnectionSignUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.roo.RooUsersConnectionRepository;

@Configuration
public class ConnectionSignUpConfig {


	// Handle to users connection repository - allows us to set connection sign up in post construct
	@Autowired
	private RooUsersConnectionRepository rooUsersConnectionRepository;
	
	@Autowired(required=false)
	private SpringSocialSecurityConnectionSignUp springSocialSecurityConnnectionSignUp;

	
	@PostConstruct
	// Registers a mechanism for implicit sign up if user id available from provider
	// Remove if explicit user name selection is required
	public void registerConnectionSignUp()
	{
		rooUsersConnectionRepository.setConnectionSignUp(springSocialSecurityConnnectionSignUp);
	}
	
}

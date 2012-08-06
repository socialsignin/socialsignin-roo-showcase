package org.socialsignin.roo.showcase.config;
/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.socialsignin.springsocial.security.ConnectInterceptorList;
import org.socialsignin.springsocial.security.signin.AuthenticatedUserIdHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.roo.RooTemplate;
import org.springframework.social.connect.roo.RooUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
 
/**
 * 
 * @author Michael Lavelle
 *
 */
@Configuration
@PropertySource("classpath:org/socialsignin/roo/showcase/socialsignin.properties")
public class SocialConfig {

	@Inject
	private Environment environment;
	
	@Inject
	private RooTemplate rooTemplate;
	
	@Inject
	private ConnectionSignUp connectionSignUp;
	
	@Bean
	@Scope(value="singleton") 
	public ConnectionFactoryRegistry connectionFactoryRegistry() {
		return new ConnectionFactoryRegistry();
	}
	
	@Bean
	@Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES) 
	public UsersConnectionRepository usersConnectionRepository() {
		
		RooUsersConnectionRepository usersConnectionRepository = new RooUsersConnectionRepository(rooTemplate, connectionFactoryRegistry(), Encryptors.noOpText());
		
		if (connectionSignUp != null && "true".equals(environment.getProperty("socialsignin.implicitSignUp")))
		{
			usersConnectionRepository.setConnectionSignUp(connectionSignUp);
		}
		
		return usersConnectionRepository;
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public ConnectionRepository connectionRepository() {
		if (getAuthenticatedLocalUserId() == null) {
			throw new IllegalStateException("Unable to get a ConnectionRepository: no local authenticated username configured");
		}
		return usersConnectionRepository().createConnectionRepository(getAuthenticatedLocalUserId());
	}

	
	private String getAuthenticatedLocalUserId()
	{
		return AuthenticatedUserIdHolder.getAuthenticatedUserId();
	}
	
}

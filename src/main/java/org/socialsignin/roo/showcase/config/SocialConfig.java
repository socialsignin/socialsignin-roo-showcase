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
import org.socialsignin.springsocial.security.config.annotation.EnableSpringSocialSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.UserIdSource;
import org.springframework.social.extension.config.annotation.EnableRooConnectionRepository;
import org.springframework.social.twitter.config.annotation.EnableTwitter;
 
/**
 * 
 * @author Michael Lavelle
 *
 */
@Configuration
@PropertySource("classpath:org/socialsignin/roo/showcase/socialsignin.properties")
// Use the following enable format if property socialsignin.implicitSignUp=true
//@EnableRooConnectionRepository(connectionSignUpRef="springSocialSecurityConnectionSignUp")
@EnableRooConnectionRepository
@EnableSpringSocialSecurity
@EnableTwitter(appId = "${twitter.consumerKey}", appSecret = "${twitter.consumerSecret}")
public class SocialConfig {
	
	@Bean
	public UserIdSource userIdSource() {
		return new UserIdSource() {			
			@Override
			public String getUserId() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null) {
					throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
				}
				return authentication.getName();
			}
		};
	}
	
	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}

	
}

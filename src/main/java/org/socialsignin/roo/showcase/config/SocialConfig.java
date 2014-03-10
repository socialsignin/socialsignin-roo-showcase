package org.socialsignin.roo.showcase.config;

import javax.sql.DataSource;

import org.socialsignin.springsocial.security.connect.SpringSocialSecurityConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.roo.RooTemplate;
import org.springframework.social.connect.roo.RooUsersConnectionRepository;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
@EnableSocial
@PropertySource("classpath:/org/socialsignin/roo/showcase/socialsignin.properties")
public class SocialConfig implements SocialConfigurer {

	@Autowired
	private RooTemplate rooTemplate;
	
	@Override
	public void addConnectionFactories(
			ConnectionFactoryConfigurer cfConfig,
			Environment env) {
		 cfConfig.addConnectionFactory(new TwitterConnectionFactory(
	                env.getProperty("twitter.consumerKey"),
	                env.getProperty("twitter.consumerSecret")
	        ));
	        cfConfig.addConnectionFactory(new SpringSocialSecurityConnectionFactory());
	}

	/**
	 * This is only needed because the official spring-social-security from SpringSocial is on the classpath
	 * @return
	 */
	@Override
	public UserIdSource getUserIdSource() {
		 return new AuthenticationNameUserIdSource();
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(
			ConnectionFactoryLocator connectionFactoryLocator) {
		RooUsersConnectionRepository rooUsersConnectionRepository = new RooUsersConnectionRepository(
				rooTemplate,
                connectionFactoryLocator,
                Encryptors.noOpText()
                
        );
		return rooUsersConnectionRepository;
		
	}
	



}

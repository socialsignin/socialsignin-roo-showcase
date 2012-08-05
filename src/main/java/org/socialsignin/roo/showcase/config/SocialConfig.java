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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
 
/**
 * Configuration to support simplest ProviderSerivce use-case of
 * making calls to third party providers which don't require
 * specific user-authentication, such as searching public
 * twitter timeline.  Only requires the consumer keys and secrets
 * for your third party provider that were set up for your
 * applicaiton to be added to a properties file, and a single
 * bean from spring-social to be registered.
 *   
 * 
 * @author Michael Lavelle
 *
 */
@Configuration
@PropertySource("classpath:org/socialsignin/roo/showcase/socialsignin.properties")
public class SocialConfig {

	@Bean
	@Scope(value="singleton") 
	public ConnectionFactoryRegistry connectionFactoryRegistry() {
		return new ConnectionFactoryRegistry();
	}
}

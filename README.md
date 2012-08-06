socialsignin-roo-showcase
=========================

This project demonstrates how to add spring-social capability to a Roo generated project, using a number of SocialSignin projects
to help developers to get up and running quickly: 

- <a href="https://github.com/socialsignin/spring-social-security" target="_blank">spring-social-security</a> is used for local-user authentication based on third party connection status, 
  removing the need for local username/password account management

- socialsignin-provider modules are used, allowing

 	- developers to obtain and interact with spring-social APIs directly for a number of common-use cases, rather than  dealing with 	       lower-level spring-social connection API
	
	- easy configuration  and registration of spring-social components through component-scanning

- <a href="https://github.com/michaellavelle/spring-social-roo-connectionrepository" target="_blank">spring-social-roo-connection-repository</a> is used to enable Roo-backed JPA persistence to be used to store connections instead
of using the Jdbc versions in the spring-social-core library.  

To run this application:

- Get the code: git clone https://github.com/socialsignin/socialsignin-roo-showcase.git
- <a href="https://dev.twitter.com/apps">Register a Twitter applicaiton</a> and add your Consumer key and Consumer secret to 
<a href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/resources/org/socialsignin/roo/showcase/socialsignin.properties">socialsignin.properties</a>
- mvn jetty:run , go to <a target="_blank" href="http://localhost:8080/socialsignin-roo-showcase">http://localhost:8080/socialsignin-roo-showcase</a>, then click on the "Start Showcase" link from the homepage.

This project was created using Roo as follows:
```
project --topLevelPackage org.socialsignin.roo.showcase --projectName socialsignin-roo-showcase --java 6 --packaging JAR
web mvc setup
security setup
jpa setup --provider HIBERNATE --database HYPERSONIC_IN_MEMORY

```
- SocialSignIn repo and dependencies added to pom:

```
  <repository>
    	<id>opensourceagility-snapshots</id>
   		<url>http://repo.opensourceagility.com/snapshots </url>
	</repository>
```

```
 <!-- Start SocialSignin dependencies -->
          	<dependency>
		<groupId>org.socialsignin</groupId>
			<artifactId>socialsignin-twitter</artifactId>
			<version>1.0.2-SNAPSHOT</version>
		</dependency>
		<dependency>
  			<groupId>javax.persistence</groupId>
  			<artifactId>persistence-api</artifactId>
  			<version>1.0</version>
  			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>cglib</groupId>
			<artifactId>cglib</artifactId>
			<version>2.2.2</version>
		</dependency>
<!-- End SocialSignin dependencies -->

```

- Showcase code created:
	
	- <a href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/java/org/socialsignin/roo/showcase/controller/ShowcaseController.java" target="_blank">ShowcaseController</a> created, having access to TwitterProviderService, 
	demonstrating use of each of the 3 use-cases supported by SocialSignIn. 
	- Creation of <a href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/webapp/WEB-INF/views/showcase.jspx>showcase.jspx</a> and creation of corresponding <a href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/webapp/WEB-INF/views/views.xml">tiles view definition</a>.

- View modifications to support this showcase code

	Added showcase instructions and a link to ShowcaseController url to <a href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/webapp/WEB-INF/views/index.jspx">index.jspx</a>

	Created <a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/webapp/WEB-INF/views/sociallogin.jspx">sociallogin.jspx</a> to support a user logging in via a third-party-provider and <a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/webapp/WEB-INF/views/signUpForm.jspx">signUpForm.jspx</a> for username selection on signup, made some minor
	amendments to <a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/webapp/WEB-INF/views/header.jspx">existing header</a> to display logged in status, and added messages to <a href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/webapp/WEB-INF/i18n/messages.properties">messages.properties</a> to support these
	views.  Created corresponding <a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/webapp/WEB-INF/views/views.xml">tiles view definitions</a>.

	AuthenticationDetailViewPreparer component created and wired into <a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/webapp/WEB-INF/layouts/default.jspx">default.jspx</a>
	default.jspx, so as to make authentication information available to all views.
	
- Config modifications to support this showcase code

	<a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/resources/org/socialsignin/roo/showcase/socialsignin.properties">Properties file created</a>

	<a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/java/org/socialsignin/roo/showcase/config/SocialConfig.java">SocialConfig</a> configuration class created
	```
	<class>org.springframework.social.connect.roo.UserConnection</class> 
	```
	added to <a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/resources/META-INF/persistence.xml" >persistence.xml</a>

	Modified <a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/resources/META-INF/spring/applicationContext.xml">applicationContext.xml</a>, <a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/resources/META-INF/spring/applicationContext-security.xml">applicationContext-security.xml</a> and <a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/webapp/WEB-INF/spring/webmvc-config.xml">webmvc-config.xml</a> as follows:


	<a target="_blank" href="https://github.com/socialsignin/socialsignin-roo-showcase/blob/master/src/main/resources/META-INF/spring/applicationContext.xml">applicationContext.xml</a>, component scan for SocialSignIn Provider modules, Spring Social Security and Roo-based UsersConnectionRepository

	```
    	<context:component-scan base-package="org.socialsignin.provider"/>
    
    	<!--  Component scan for Spring Social Security  -->
    	<context:component-scan base-package="org.socialsignin.springsocial.security"/>
    
    	<!--  Component scan for Roo managed peristence for ConnectionRepository  -->
    	<context:component-scan base-package="org.springframework.social.connect.roo">
        	<context:exclude-filter expression=".*_Roo_.*" type="regex"/>
    	</context:component-scan>   
	``` 

package com.sofyan.erv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.persistence.EntityManager;

@SpringBootApplication
public class EntityRelationshipVisualizationApplication {

	@Autowired
	private EntityManager em;

	public static void main(String[] args) {
		SpringApplication.run(EntityRelationshipVisualizationApplication.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void doMap() {
//
//		JpaHelper.getRelation( this.em );
//
//	}

	@Configuration
	@EnableWebSecurity
	public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception{
			http.authorizeRequests().antMatchers("/").permitAll();
		}
	}

}


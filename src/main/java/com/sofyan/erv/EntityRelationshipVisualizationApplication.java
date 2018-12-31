package com.sofyan.erv;

import javax.servlet.ServletContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.multipart.support.MultipartFilter;

@SpringBootApplication(
		exclude = { DataSourceAutoConfiguration.class, 
				DataSourceTransactionManagerAutoConfiguration.class, 
				HibernateJpaAutoConfiguration.class
				})
public class EntityRelationshipVisualizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntityRelationshipVisualizationApplication.class, args);
	}

	@Configuration
	@EnableWebSecurity
	public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception{
			http.authorizeRequests().antMatchers("/").permitAll();
		}
	}

	@Configuration
	public class SecurityApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

		@Override
		protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
			insertFilters(servletContext, new MultipartFilter());
		}
	}

}


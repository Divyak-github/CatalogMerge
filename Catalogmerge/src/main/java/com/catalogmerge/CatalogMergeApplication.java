package com.catalogmerge;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author divya
 *
 */
	
	@SpringBootApplication
	@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
	@ComponentScan(basePackages = {"com.catalogmerge.controller","com.catalogmerge.service"})
	@EnableJpaRepositories(basePackages ={"com.catalogmerge.repository"})
	@EntityScan("com.catalogmerge.dao")
	@EnableTransactionManagement
	public class CatalogMergeApplication {

		public static void main(String[] args) {
			SpringApplication.run(CatalogMergeApplication.class, args);
			
		}
}


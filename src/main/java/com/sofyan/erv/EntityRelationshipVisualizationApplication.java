package com.sofyan.erv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.persistence.EntityManager;

@SpringBootApplication
public class EntityRelationshipVisualizationApplication {

	@Autowired
	private EntityManager em;

	public static void main(String[] args) {
		SpringApplication.run(EntityRelationshipVisualizationApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doMap() {

//		String tableName = JpaHelper.getTableName( this.em, Product.class);
//		System.out.println( tableName + " !!!!!!!!");

		JpaHelper.getRelation( this.em );

	}

}


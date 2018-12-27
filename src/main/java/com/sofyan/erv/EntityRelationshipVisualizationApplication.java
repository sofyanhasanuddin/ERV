package com.sofyan.erv;

import com.sofyan.erv.helper.JpaHelper;
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

//	@EventListener(ApplicationReadyEvent.class)
//	public void doMap() {
//
//		JpaHelper.getRelation( this.em );
//
//	}

}


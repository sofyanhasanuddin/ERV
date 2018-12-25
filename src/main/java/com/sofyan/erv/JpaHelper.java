package com.sofyan.erv;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.Map;
import java.util.Set;

public class JpaHelper {

    /**
     * Returns the table name for a given entity type in the {@link EntityManager}.
     * @param em
     * @param entityClass
     * @return
     */
    public static <T> String getTableName(EntityManager em, Class<T> entityClass) {
        /*
         * Check if the specified class is present in the metamodel.
         * Throws IllegalArgumentException if not.
         */
        Metamodel meta = em.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);

        //Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);

        String tableName = (t == null)
                ? entityType.getName().toUpperCase()
                : t.name();
        return tableName;
    }

    public static Map<String,String> getRelation(EntityManager em) {

        Metamodel meta = em.getMetamodel();
        Set<EntityType<?>> setEntity = meta.getEntities();

        setEntity
                .stream()
                .forEach( entityType -> {
                    entityType.getAttributes().stream().forEach( attribute -> {
                        System.out.println( attribute);
                    });
                });

        return null;

    }

}

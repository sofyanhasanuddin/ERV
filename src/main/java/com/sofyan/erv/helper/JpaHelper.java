package com.sofyan.erv.helper;

import com.sofyan.erv.response.EntityInfoResponse;
import com.sofyan.erv.response.EntityProperty;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JpaHelper {

    private static <T> String getTableName(EntityType<T> entityType) {

        Class<T> entityClass = entityType.getJavaType();

        Table t = entityClass.getAnnotation(Table.class);

        String tableName = (t == null)
                ? entityType.getName().toUpperCase()
                : t.name();

        return tableName;
    }

    public static List<EntityInfoResponse> getInfo(EntityManager em) {

        Metamodel meta = em.getMetamodel();
        Set<EntityType<?>> setEntity = meta.getEntities();

        List<EntityInfoResponse> list = new ArrayList<>();

        setEntity
                .stream()
                .forEach( entityType -> {

                    EntityInfoResponse eir = new EntityInfoResponse();
                    eir.setClassName( entityType.getName() );
                    eir.setTableName( getTableName(entityType));

                    entityType.getAttributes().stream().forEach( attribute -> {

                        EntityProperty ep = new EntityProperty();
                        ep.setName( attribute.getName() );
                        ep.setJavaClass( attribute.getJavaType().getName() );
                        ep.setAttributeType( attribute.getPersistentAttributeType() );

                        try {

                            Field field = entityType.getJavaType().getField(attribute.getName());
                            ep.setOwnRelation( field.getAnnotation(JoinColumn.class) == null ? false : true );

                        } catch (NoSuchFieldException e) {
                            ep.setOwnRelation( false );
                        }

                        eir.getListProperty().add( ep );

                    });

                    list.add( eir );

                });

        return list;

    }

}

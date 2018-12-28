package com.sofyan.erv.helper;

import com.sofyan.erv.response.EntityInfoResponse;
import com.sofyan.erv.response.EntityProperty;
import io.github.classgraph.*;

import javax.persistence.*;
import javax.persistence.metamodel.Attribute;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RelationHelper {

    public static List<EntityInfoResponse> findAllClass(String packg) {

        List<EntityInfoResponse> list = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages( packg )
                .scan()) {

            scanResult
                    .getClassesWithAnnotation(Entity.class.getName())
                    .stream()
                    .forEach(classInfo -> {

                        String tableName = classInfo.getName();

                        if (classInfo.hasAnnotation(Table.class.getName())) {
                            tableName = classInfo
                                    .getAnnotationInfo(Table.class.getName())
                                    .getParameterValues()
                                    .get("name").toString();
                        }

                        EntityInfoResponse eir = new EntityInfoResponse();
                        eir.setClassName(classInfo.getName());
                        eir.setTableName(tableName);

                        classInfo
                                .getFieldInfo()
                                .stream()
                                .forEach(fieldInfo -> {

                                    boolean isColumn = false;

                                    EntityProperty ep = new EntityProperty();
                                    ep.setName(fieldInfo.getName());
                                    ep.setJavaClass(fieldInfo.getTypeSignatureOrTypeDescriptor().toString());

                                    if (fieldInfo.hasAnnotation(OneToOne.class.getName())) {

                                        isColumn = true;
                                        ep.setAttributeType(Attribute.PersistentAttributeType.ONE_TO_ONE);
                                        ep.setRelationClass( getRelationClassForNonGeneric(fieldInfo));

                                    }else if (fieldInfo.hasAnnotation(ManyToOne.class.getName())) {

                                        isColumn = true;
                                        ep.setAttributeType(Attribute.PersistentAttributeType.MANY_TO_ONE);
                                        ep.setRelationClass( getRelationClassForNonGeneric(fieldInfo));

                                    } else if (fieldInfo.hasAnnotation(OneToMany.class.getName())) {

                                        isColumn = true;
                                        ep.setAttributeType(Attribute.PersistentAttributeType.ONE_TO_MANY);
                                        ep.setRelationClass( getRelationClassForGeneric(classInfo,fieldInfo));

                                    } else if (fieldInfo.hasAnnotation(ManyToMany.class.getName())) {

                                        isColumn = true;
                                        ep.setAttributeType(Attribute.PersistentAttributeType.MANY_TO_MANY);
                                        ep.setRelationClass( getRelationClassForGeneric(classInfo,fieldInfo));

                                    } else if (fieldInfo.hasAnnotation(Embedded.class.getName())) {

                                        isColumn = true;
                                        ep.setAttributeType(Attribute.PersistentAttributeType.EMBEDDED);
                                        ep.setRelationClass( getRelationClassForNonGeneric(fieldInfo));

                                    } else if (fieldInfo.hasAnnotation(Column.class.getName())) {

                                        isColumn = true;
                                        ep.setAttributeType(Attribute.PersistentAttributeType.BASIC);

                                    }

                                    if (isColumn) {

                                        if (fieldInfo.hasAnnotation(JoinColumn.class.getName()))
                                            ep.setOwnRelation(true);

                                        eir.getListProperty().add(ep);
                                    }


                                });

                        list.add(eir);


                    });

            return list;
        }

    }

    private static String getRelationClassForNonGeneric(FieldInfo fieldInfo ) {

        return fieldInfo.getTypeSignatureOrTypeDescriptor().toString();

    }

    private static String getRelationClassForGeneric(ClassInfo classInfo,FieldInfo fieldInfo ) {

        TypeSignature generic = fieldInfo.getTypeSignatureOrTypeDescriptor();

        if( generic != null ) {
            try {
                Class c = Class.forName(classInfo.getName());
                Field f = c.getDeclaredField( fieldInfo.getName() );
                return ((Class) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]).getName();
            } catch (Exception _e) {
                _e.printStackTrace();
            }
        }

        return null;

    }


}

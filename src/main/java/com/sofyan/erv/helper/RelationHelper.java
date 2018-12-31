package com.sofyan.erv.helper;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.metamodel.Attribute;

import com.sofyan.erv.response.EntityInfoResponse;
import com.sofyan.erv.response.EntityProperty;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.ScanResult;

public class RelationHelper {

    public static Map<String,EntityInfoResponse> findAllClass(final String file,
    		final String pkg) {

        Map<String,EntityInfoResponse> result = new HashMap<>();

        try (ScanResult scanResult = new ClassGraph()
                .verbose()
                .overrideClasspath(file)
                .whitelistPackages(pkg)
                .enableAllInfo()
                .scan()) {

            Integer index = 0;

            for (ClassInfo classInfo : scanResult
                    .getClassesWithAnnotation(Entity.class.getName())) {

                String tableName = classInfo.getName();

                if (classInfo.hasAnnotation(Table.class.getName())) {
                	try {
                		tableName = classInfo
                            .getAnnotationInfo(Table.class.getName())
                            .getParameterValues()
                            .get("name").toString();
                	}catch (Exception e) {
						e.printStackTrace();
					}
                }

                EntityInfoResponse eir = new EntityInfoResponse();
                eir.setClassNameWithPackage(classInfo.getName());
                eir.setClassName(classInfo.getSimpleName());
                eir.setTableName(tableName);
                eir.setId( index++ );

                classInfo
                        .getFieldInfo()
                        .stream()
                        .forEach(fieldInfo -> {

                            boolean isColumn = false;

                            EntityProperty ep = new EntityProperty();
                            ep.setName(fieldInfo.getName());
                            ep.setClassNameWithPackage(fieldInfo.getTypeSignatureOrTypeDescriptor().toString());
                            ep.setClassName( StringUtil.getSimpleClassName(ep.getClassNameWithPackage()));

                            if (fieldInfo.hasAnnotation(OneToOne.class.getName())) {

                                isColumn = true;
                                ep.setAttributeType(Attribute.PersistentAttributeType.ONE_TO_ONE);
                                ep.setRelationClass(getRelationClassForNonGeneric(fieldInfo));

                            } else if (fieldInfo.hasAnnotation(ManyToOne.class.getName())) {

                                isColumn = true;
                                ep.setAttributeType(Attribute.PersistentAttributeType.MANY_TO_ONE);
                                ep.setRelationClass(getRelationClassForNonGeneric(fieldInfo));

                            } else if (fieldInfo.hasAnnotation(OneToMany.class.getName())) {

                                isColumn = true;
                                ep.setAttributeType(Attribute.PersistentAttributeType.ONE_TO_MANY);
                                ep.setRelationClass(getRelationClassForGeneric(classInfo, fieldInfo));

                            } else if (fieldInfo.hasAnnotation(ManyToMany.class.getName())) {

                                isColumn = true;
                                ep.setAttributeType(Attribute.PersistentAttributeType.MANY_TO_MANY);
                                ep.setRelationClass(getRelationClassForGeneric(classInfo, fieldInfo));

                            } else if (fieldInfo.hasAnnotation(Embedded.class.getName())) {

                                isColumn = true;
                                ep.setAttributeType(Attribute.PersistentAttributeType.EMBEDDED);
                                ep.setRelationClass(getRelationClassForNonGeneric(fieldInfo));

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

                result.put( eir.getClassNameWithPackage(),eir );
            }

            return result;
        }

    }

    private static String getRelationClassForNonGeneric(FieldInfo fieldInfo) {

        return fieldInfo.getTypeSignatureOrTypeDescriptor().toString();

    }

    private static String getRelationClassForGeneric(ClassInfo classInfo, FieldInfo fieldInfo) {
        
        return StringUtil.getGenericClass( fieldInfo.getTypeSignatureOrTypeDescriptor().toString() );

//        if (generic != null) {
//            try {
//                Class c = Class.forName(classInfo.getName());
//                Field f = c.getDeclaredField(fieldInfo.getName());
//                return ((Class) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]).getName();
//            } catch (Exception _e) {
//                _e.printStackTrace();
//            }
//        }
//
//        return null;

    }


}

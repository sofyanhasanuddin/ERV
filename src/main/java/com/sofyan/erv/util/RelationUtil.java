package com.sofyan.erv.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import org.springframework.util.CollectionUtils;

import com.sofyan.erv.response.EntityInfo;
import com.sofyan.erv.response.EntityProperty;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.ScanResult;

public class RelationUtil {

    public static Map<String,EntityInfo> findAllClass(final String file,
    		final String pkg) {

        Map<String,EntityInfo> result = new HashMap<>();

        try (ScanResult scanResult = new ClassGraph()
                .overrideClasspath(file)
                .whitelistPackages(pkg)
                .enableAllInfo()
                .scan()) {

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
                		//silently
					}
                }

                EntityInfo eir = new EntityInfo();
                eir.setClassNameWithPackage(classInfo.getName());
                eir.setClassName(classInfo.getSimpleName());
                eir.setTableName(tableName);
                eir.setId( UUID.randomUUID().toString());

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
    
    public static EntityInfo findOneClass(final String file,
    		final String pkgWithClassName) {
    	
    	EntityInfo eir = new EntityInfo();

        try (ScanResult scanResult = new ClassGraph()
                .overrideClasspath(file)
                .whitelistPackages( StringUtil.getPackageFromClass(pkgWithClassName) )
                .enableAllInfo()
                .scan()) {
        	
        	List<ClassInfo> l = scanResult.getClassesWithAnnotation(Entity.class.getName());
        	if( CollectionUtils.isEmpty(l) )
        		throw new RuntimeException("Failed to get specifiec class with parameter : " + pkgWithClassName);
        	
        	String className = StringUtil.getSimpleClassName(pkgWithClassName);
        	
        	ClassInfo classInfo =  null;
        	
        	for (ClassInfo classInfoInList : l) {
				if( classInfoInList.getSimpleName().equals( className) ) {
					classInfo = classInfoInList;
					break;
				}
			}
        	
        	if( classInfo == null )
        		throw new RuntimeException("Cannot get Class : " + pkgWithClassName );

            String tableName = classInfo.getName();

            if (classInfo.hasAnnotation(Table.class.getName())) {
            	try {
            		tableName = classInfo
                        .getAnnotationInfo(Table.class.getName())
                        .getParameterValues()
                        .get("name").toString();
            	}catch (Exception e) {
            		//silently
				}
            }
            
            eir.setClassNameWithPackage(classInfo.getName());
            eir.setClassName(classInfo.getSimpleName());
            eir.setTableName(tableName);
            eir.setId( UUID.randomUUID().toString() );

	        classInfo
                .getFieldInfo()
                .stream()
                .forEach(fieldInfo -> {

                    EntityProperty ep = new EntityProperty();
                    ep.setName(fieldInfo.getName());
                    ep.setClassNameWithPackage(fieldInfo.getTypeSignatureOrTypeDescriptor().toString());
                    ep.setClassName( StringUtil.getSimpleClassName(ep.getClassNameWithPackage()));
                    eir.getListProperty().add(ep);
                    
                });
            }
            
            return eir;
        }

    private static String getRelationClassForNonGeneric(FieldInfo fieldInfo) {

        return fieldInfo.getTypeSignatureOrTypeDescriptor().toString();

    }

    private static String getRelationClassForGeneric(ClassInfo classInfo, FieldInfo fieldInfo) {
        
        return StringUtil.getGenericClass( fieldInfo.getTypeSignatureOrTypeDescriptor().toString() );

    }


}

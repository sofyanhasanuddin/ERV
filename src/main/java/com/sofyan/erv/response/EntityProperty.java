package com.sofyan.erv.response;

import javax.persistence.metamodel.Attribute;
import java.io.Serializable;

public class EntityProperty implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Attribute.PersistentAttributeType attributeType;
    private String name;
    private String classNameWithPackage;
    private String className;
    private String relationClass;
    private boolean ownRelation;

    public Attribute.PersistentAttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(Attribute.PersistentAttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassNameWithPackage() {
		return classNameWithPackage;
	}

	public void setClassNameWithPackage(String classNameWithPackage) {
		this.classNameWithPackage = classNameWithPackage;
	}

	public boolean isOwnRelation() {
        return ownRelation;
    }

    public void setOwnRelation(boolean ownRelation) {
        this.ownRelation = ownRelation;
    }

    public String getRelationClass() {
        return relationClass;
    }

    public void setRelationClass(String relationClass) {
        this.relationClass = relationClass;
    }

}

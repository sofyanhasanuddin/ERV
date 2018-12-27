package com.sofyan.erv.response;

import javax.persistence.metamodel.Attribute;
import java.io.Serializable;

public class EntityProperty implements Serializable {

    private Attribute.PersistentAttributeType attributeType;
    private String name;
    private String javaClass;
    private boolean ownRelation;

    public Attribute.PersistentAttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(Attribute.PersistentAttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    public boolean isOwnRelation() {
        return ownRelation;
    }

    public void setOwnRelation(boolean ownRelation) {
        this.ownRelation = ownRelation;
    }

}

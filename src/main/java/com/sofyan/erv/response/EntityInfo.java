package com.sofyan.erv.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntityInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
    private String classNameWithPackage;
    private String className;
    private String tableName;
    private List<EntityProperty> listProperty = new ArrayList<>();
    private String color;
    
    public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getClassNameWithPackage() {
		return classNameWithPackage;
	}

	public void setClassNameWithPackage(String classNameWithPackage) {
		this.classNameWithPackage = classNameWithPackage;
	}

	public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<EntityProperty> getListProperty() {
        return listProperty;
    }

    public void setListProperty(List<EntityProperty> listProperty) {
        this.listProperty = listProperty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}

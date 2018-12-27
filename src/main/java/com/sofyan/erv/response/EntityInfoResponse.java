package com.sofyan.erv.response;

import java.util.ArrayList;
import java.util.List;

public class EntityInfoResponse {

    private String className;
    private String tableName;
    private List<EntityProperty> listProperty = new ArrayList<>();

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
}

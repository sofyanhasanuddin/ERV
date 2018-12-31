package com.sofyan.erv.response;

public class Link {

	private Integer source;
	private Integer target;
	private String type;
	private String fieldRelation;

	public String getFieldRelation() {
		return fieldRelation;
	}

	public void setFieldRelation(String fieldRelation) {
		this.fieldRelation = fieldRelation;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

package com.sofyan.erv.response;

public class Link {

	private String source;
	private String target;
	private String type;
	private String fieldRelation;

	public String getFieldRelation() {
		return fieldRelation;
	}

	public void setFieldRelation(String fieldRelation) {
		this.fieldRelation = fieldRelation;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

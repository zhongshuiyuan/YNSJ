package com.titan.ynsjy.entity;

import java.io.Serializable;

public class Row implements Serializable{

	/* */
	private static final long serialVersionUID = 1L;
	public String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String name;
}

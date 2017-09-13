package com.titan.ynsjy.entity;

import com.titan.ynsjy.db.sqlite.Column;
import com.titan.ynsjy.db.sqlite.Id;
import com.titan.ynsjy.db.sqlite.Table;

@Table(name = "User")
public class User {
	
	@Id(autoGenerate = true, name = "Id")
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "ic_password")
	private String psw;
	
	public User() {
		
	}
	
	public User(String name,String pwd) {
		this.name = name;
		this.psw = pwd;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPsw() {
		return psw;
	}
	public void setPsw(String psw) {
		this.psw = psw;
	}
	
}

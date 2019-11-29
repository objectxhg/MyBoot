package com.xhg.pojo;

import java.io.Serializable;

public class Role implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String roleName;

	public Integer getId() {
		return id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", roleName=" + roleName + "]";
	}
	
	
}

package com.xhg.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户")
public class sysUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("用户id")
	private Integer id;
	@ApiModelProperty(value = "用户姓名")
	private String username;
	@ApiModelProperty(value = "密码")
	private String pwd;
	@ApiModelProperty(value = "手机号")
	private String mobile;
	@ApiModelProperty(value = "生日")
	private String birthday;
	@ApiModelProperty(value = "地址")
	private String address;
	
	private List<Role> roleList = new ArrayList<Role>();
	
	
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", pwd=" + pwd + ", mobile=" + mobile + ", birthday="
				+ birthday + ", address=" + address + ", roleList=" + roleList + "]";
	}
	
	
}

package com.xhg.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable{
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private Integer id;
		private String username;
		private String pwd;
		private String mobile;
		private String birthday;
		private String address;
		
		private List<Role> roleList = new ArrayList<Role>();
		
		
		public List<Role> getRoleList() {
			return roleList;
		}
		public void setRoleList(List<Role> roleList) {
			this.roleList = roleList;
		}
		
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getPwd() {
			return pwd;
		}
		public void setPwd(String pwd) {
			this.pwd = pwd;
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
		
		
}

package com.xhg.pojo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class User {
	
		private Integer id;
		private String name;
		
		//标识格式
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private Date birthday;
		private String address;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
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
			return "User [id=" + id + ", name=" + name + ", birthday=" + birthday + ", address=" + address + "]";
		}
		
		
}

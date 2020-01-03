package com.xhg.pojo;

public class Message {	
	
	private Integer id;
	private String content;
	public Integer getId() {
		return id;
	}
	public String getContent() {
		return content;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", content=" + content + "]";
	}
	
}

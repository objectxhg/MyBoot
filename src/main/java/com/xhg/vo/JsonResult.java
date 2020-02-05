package com.xhg.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)  // 属性为 空（“”） 或者为 NULL 都不序列化 
//@JsonInclude(JsonInclude.Include.NON_NULL) // 属性为NULL 不序列化
@SuppressWarnings("all")
public class JsonResult<T> {
	
	private static final Boolean ERROR = Boolean.FALSE; 
	private static final Boolean SUCCESS = Boolean.TRUE; 
	
	private static final Integer EXCEPTION_STATE = 500;
	
	private Boolean state;
	private Integer code;
    private String message;
    private T data;
    
    public JsonResult() {
        super();
    }
    
    public JsonResult(Exception e) {
    	this.state = ERROR;
    	this.code = EXCEPTION_STATE;
    	this.message = e.getMessage();
    }
    
    public JsonResult(Boolean state,String message,T data) {
    	this.state = state;
    	this.message = message;
    	this.data = data;
    }
    
    public JsonResult(Boolean state,String message) {
    	this.state = state;
    	this.message = message;
    }
    
    public JsonResult(Boolean state,T data) {
    	this.state = state;
    	this.data = data;
    }
    
    
	public static JsonResult success(Object data) {
		
    	return new JsonResult(SUCCESS, data);
    }
	
	public static JsonResult success(String message) {
		
    	return new JsonResult(SUCCESS, message);
    }
	
	public static JsonResult fail(String message) {
		
    	return new JsonResult(ERROR, message);
    }
	
    
	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "JsonResult [state=" + state + ", code=" + code + ", message=" + message + ", data=" + data + "]";
	}

    
}

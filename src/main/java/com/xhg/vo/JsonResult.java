package com.xhg.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @JsonInclude(JsonInclude.Include.NON_NULL) // 属性为NULL 不序列化
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)  // 属性为 空（“”） 或者为 NULL 都不序列化
@SuppressWarnings("all")
public class JsonResult<T> {
	
	private static final Boolean ERROR = Boolean.FALSE; 
	private static final Boolean SUCCESS = Boolean.TRUE; 
	
	private static final Integer EXCEPTION_CODE = 500;
	private static final Integer SUCCESS_CODE = 200;

	private static final String SUCCESS_MESSAGE = "请求成功";

	
	private Boolean state;
	private Integer code;
    private String message;
    private T data;
    
    public JsonResult() {
        super();
    }
    
    public JsonResult(Exception e) {
    	this.code = EXCEPTION_CODE;
    	this.message = e.getMessage();
    }

    public JsonResult(Integer code, String message) {
    	this.code = code;
    	this.message = message;
    }


	public JsonResult(Integer code, T data, String message) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

    
	public static JsonResult success(Object data, String message) {
		
    	return new JsonResult(SUCCESS_CODE, data, message);
    }

	public static JsonResult success(Object data) {

		return new JsonResult(SUCCESS_CODE, data, SUCCESS_MESSAGE);
	}

	public static JsonResult success(String message) {

    	return new JsonResult(SUCCESS_CODE, message);
    }

	public static JsonResult success(Integer code, String message) {

		return new JsonResult(code, message);
	}

	public static JsonResult fail(String message) {

		return new JsonResult(EXCEPTION_CODE, message);
	}

	public static JsonResult fail(Integer code, String message) {

		return new JsonResult(code, message);
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

package com.xhg.pojo;

public class ApiResponse {

    private String errCode;

    private Object data;

    private String errMsg;

    @Override
    public String toString() {
        return "ApiResponse{" +
                "errCode='" + errCode + '\'' +
                ", data=" + data +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}

package com.xhg.pojo;

import java.io.Serializable;

public class ApiRequest implements Serializable {

    private String apiId;

    private String sid;

    private String method;

    private String version;

    private String timestamp;

    private String signType;

    private String bizParams;

    private String sign;

    @Override
    public String toString() {
        return "ApiRequest{" +
                "apiId='" + apiId + '\'' +
                ", sid='" + sid + '\'' +
                ", method='" + method + '\'' +
                ", version='" + version + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", signType='" + signType + '\'' +
                ", bizParams='" + bizParams + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public String getBizParams() {
        return bizParams;
    }

    public void setBizParams(String bizParams) {
        this.bizParams = bizParams;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }
}

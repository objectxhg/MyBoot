package com.xhg.vo;

import java.io.Serializable;

/**
 * @Author xiaoh
 * @create 2020/9/2 18:22
 */
public class AccessToken implements Serializable {

    private String access_token;
    private Integer expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {

        this.access_token = access_token;
    }

    public Integer getExpires_in() {

        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {

        this.expires_in = expires_in;
    }
}


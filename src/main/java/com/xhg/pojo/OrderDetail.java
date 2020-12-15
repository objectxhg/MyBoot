package com.xhg.pojo;

import io.swagger.annotations.ApiModelProperty;

import javax.xml.soap.Detail;

/**
 * @Author xiaoh
 * @create 2020/12/15 10:23
 */
public class OrderDetail {

    public OrderDetail(){}

    public OrderDetail(Long orderId, String detail){
        this.orderId = orderId;
        this.detail = detail;
    }

    @ApiModelProperty("订单详情Id")
    private Integer id;

    @ApiModelProperty("订单Id")
    private Long orderId;

    @ApiModelProperty("订单详情")
    private String detail;
}


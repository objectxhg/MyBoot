package com.xhg.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author xiaoh
 * @create 2020/8/24 15:24
 */

@Data
@ApiModel("订单")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    public Order(){

    }

    public Order(Integer userId, String orderDescribe){
        this.userId = userId;
        this.orderDescribe = orderDescribe;
    }

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("订单描述")
    private String orderDescribe;

    @ApiModelProperty("下单时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date orderUpdateTime;

}


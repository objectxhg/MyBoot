package com.xhg.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author xiaoh
 * @create 2020/8/24 15:24
 */

@Data
@ApiModel("订单")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

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


}


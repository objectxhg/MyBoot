package com.xhg.mapper;

import com.xhg.pojo.Order;

import java.util.List;

/**
 * @Author xiaoh
 * @create 2020/8/24 15:41
 */
public interface OrderMapper {

    Integer createOrder(Order order);

    List<Order> selectOrder();
}


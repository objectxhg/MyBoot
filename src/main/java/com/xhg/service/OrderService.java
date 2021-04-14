package com.xhg.service;

import com.github.pagehelper.PageInfo;
import com.xhg.pojo.Order;
import java.util.List;

/**
 * @Author xiaoh
 * @Description:
 * @create 2020/8/24 15:53
 */
public interface OrderService {

    Integer addOrder(Order order);

    List<Order> orderList (Integer pageNum, Integer pageSize);

}

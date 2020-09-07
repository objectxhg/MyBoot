package com.xhg.mapper;

import com.xhg.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author xiaoh
 * @create 2020/8/24 15:41
 */
public interface OrderMapper {

    Integer createOrder(Order order);

    List<Order> selectOrder();

    Integer addlog(@Param("username") String username, @Param("pwd") String pwd, @Param("mobile") String mobile);
}


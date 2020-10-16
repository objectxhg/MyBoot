package com.xhg.service.Impl;

import com.xhg.mapper.OrderMapper;
import com.xhg.mapper.SysUserMapper;
import com.xhg.pojo.Order;
import com.xhg.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * @Author xiaoh
 * @create 2020/8/24 15:56
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SysUserMapper sysUserMapper;


    @Override
    public Integer addOrder(Order order) {
        if(null == sysUserMapper.getUserInfo(order.getUserId())){
                return  0;
        }
        order.setOrderUpdateTime(new Date());
        return orderMapper.createOrder(order);
    }

    @Override
    public List<Order> orderList() {
        return orderMapper.selectOrder();
    }
}


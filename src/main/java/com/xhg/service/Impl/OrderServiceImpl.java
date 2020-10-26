package com.xhg.service.Impl;

import com.xhg.mapper.OrderMapper;
import com.xhg.mapper.SysUserMapper;
import com.xhg.pojo.Order;
import com.xhg.service.OrderService;
import com.xhg.utils.SnowflakeIdWorker;
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
        /**
         * 时间戳唯一工具类ID ：order.setOrderId(SnowflakeUtil.getSnowflakeID()); 缺点：18位字符太长了，浪费而且也不好看
         *
         * 利用redis得单线程原子性存一个数值每次插入incr 来生成序列，需要做持久化AOF 和 RDB，redis挂掉重启时，由于incr命令的特殊性，恢复数据慢 （具体原因不记得了）
         *
         * 数据库序列 mycat配置（当前使用）缺点（已使用mycat配置 默认不插入id 就是使用数据库序列id） 使用数据库序列的DB可能会宕机，无发抗住高并发
         * insert
         * id 写为：next value for MYCATSEQ_ORDERS
         *
         * 雪花算法
         */
        order.setOrderId(SnowflakeIdWorker.generateId());
        order.setOrderUpdateTime(new Date());
        return orderMapper.createOrder(order);
    }

    @Override
    public List<Order> orderList() {
        return orderMapper.selectOrder();
    }
}


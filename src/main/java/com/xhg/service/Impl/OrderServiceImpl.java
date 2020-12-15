package com.xhg.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xhg.mapper.OrderDetailMapper;
import com.xhg.mapper.OrderMapper;
import com.xhg.mapper.SysUserMapper;
import com.xhg.pojo.Order;
import com.xhg.pojo.OrderDetail;
import com.xhg.service.OrderService;
import com.xhg.utils.SnowflakeIdWorker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    private OrderDetailMapper orderDetailMapper;


    @Override
    /**
     *  isolation = Isolation.READ_UNCOMMITTED 读取未提交数据(会出现脏读, 不可重复读) 基本不使用
     *
     */

    public Integer addOrder(Order order) {

        if(null == sysUserMapper.getUserInfo(order.getUserId())){
                return  0;
        }
        /**
         * 时间戳唯一工具类ID ：order.setOrderId(SnowflakeUtil.getSnowflakeID()); 缺点：18位字符太长了，浪费而且也不好看
         *
         * 利用redisd单线程原子性存一个数值每次插入incr 来生成序列，需要做持久化AOF 和 RDB，redis挂掉重启时，由于incr命令的特殊性，恢复数据慢 （具体原因不记得了）
         *
         * 数据库序列 mycat配置 缺点（mycat配置 默认不插入id 就是使用数据库序列id） 使用数据库序列的DB可能会宕机，无发抗住高并发
         * insert
         * id 写为：next value for MYCATSEQ_ORDERS
         *
         * 雪花算法
         */
        Long orderId = SnowflakeIdWorker.generateId();
        order.setOrderId(orderId);
        order.setOrderUpdateTime(new Date());

        Integer state = orderMapper.createOrder(order);
        /**
         * mycat 父子表
         *
         * 注意 由于开启了事务， 整个事务还未完成 刚插入的订单数据 没有commit
         *
         * 需要给下面这条sql 指定（READ_UNCOMMITTED）读取未提交数据事务 不然子表插入时是查询不到父表的id的
         */
        if (state == 1) return orderDetailMapper.insert(new OrderDetail(orderId, "分表-子表详情"));

        return 0;
    }

    @Override
    public List<Order> orderList(Integer pageNum, Integer pageSize) {
        return orderMapper.selectOrder();
    }
}


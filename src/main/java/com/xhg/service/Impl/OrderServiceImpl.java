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
@Transactional
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

        /**
         * mycat 父子表 ER分片插入异常
         * https://blog.csdn.net/weixin_34347651/article/details/91914864
         * 当父表和子表数据在一个事务中提交时，如果字表的parentId不是父表的分片字段，
         * 例如基本配置中的parentId为父表的ID字段，但是父表的分片字段为user_code不是ID。同时提交报错如下：
         * [Err] 1064 - can't find (root) parent sharding node for sql:。 ER分片需要查询父表所在的分片，如果是ER关系的表是在一个事务中插入数据是不可以的，只能分开提交。 或者使用冗余字段放弃使用ER分片。
         */

//        if (state == 1) return orderDetailMapper.insert(new OrderDetail(orderId, "分表-子表详情"));

        return orderMapper.createOrder(order);
    }

    @Override
    public List<Order> orderList(Integer pageNum, Integer pageSize) {
        return orderMapper.selectOrder();
    }
}


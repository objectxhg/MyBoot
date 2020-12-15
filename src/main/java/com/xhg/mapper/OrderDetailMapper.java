package com.xhg.mapper;

import com.xhg.pojo.OrderDetail;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author xiaoh
 * @create 2020/12/15 10:26
 */

public interface OrderDetailMapper {

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    Integer insert(OrderDetail detail);
}


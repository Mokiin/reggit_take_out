package com.mokiin.reggie.service;

import com.mokiin.reggie.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author mokiin
 * @since 2022-08-23
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 提交订单
     * @param orders
     */
    void submit(Orders orders);
}

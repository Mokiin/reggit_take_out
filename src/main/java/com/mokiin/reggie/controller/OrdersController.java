package com.mokiin.reggie.controller;


import com.mokiin.reggie.conmmon.Result;
import com.mokiin.reggie.pojo.Orders;
import com.mokiin.reggie.service.OrdersService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author mokiin
 * @since 2022-08-23
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    /**
     * 提交订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return Result.ok();
    }

}


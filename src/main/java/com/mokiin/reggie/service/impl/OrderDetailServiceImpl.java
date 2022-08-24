package com.mokiin.reggie.service.impl;

import com.mokiin.reggie.pojo.OrderDetail;
import com.mokiin.reggie.mapper.OrderDetailMapper;
import com.mokiin.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author mokiin
 * @since 2022-08-23
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}

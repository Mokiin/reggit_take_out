package com.mokiin.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mokiin.reggie.conmmon.BaseContext;
import com.mokiin.reggie.conmmon.CustoException;
import com.mokiin.reggie.mapper.OrdersMapper;
import com.mokiin.reggie.pojo.*;
import com.mokiin.reggie.service.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author mokiin
 * @since 2022-08-23
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Resource
    private UserService userService;

    @Resource
    private AddressBookService addressBookService;

    @Resource
    private OrderDetailService orderDetailService;

    @Transactional
    @Override
    public void submit(Orders orders) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // 获得当前用户id
            Long userId = BaseContext.getCurrentId();
            // 查询用户当前购物车数据
            LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ShoppingCart::getUserId, userId);
            // 当前用户购物车数据
            List<ShoppingCart> list = shoppingCartService.list(wrapper);
            if (list == null || list.size() == 0) {
                throw new CustoException("购物车为空");
            }
            // 查询用户数据
            User user = userService.getById(userId);
            // 查询地址数据
            String address = orders.getAddress();
            AddressBook addressBook = addressBookService.getById(address);
            if (null == addressBook) {
                throw new CustoException("地址信息为空");
            }
            // 订单号
            long orderId = IdWorker.getId();
            AtomicInteger amont = new AtomicInteger(0);

            List<OrderDetail> orderDetails = list.stream().map(item -> {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(orderId);
                orderDetail.setNumber(item.getNumber());
                orderDetail.setDishFlavor(item.getDishFlavor());
                orderDetail.setDishId(item.getDishId());
                orderDetail.setSetmealId(item.getSetmealId());
                orderDetail.setName(item.getName());
                orderDetail.setImage(item.getImage());
                orderDetail.setAmount(item.getAmount());
                amont.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
                return orderDetail;
            }).collect(Collectors.toList());
            orders.setNumber(String.valueOf(orderId));
            orders.setId(orderId);
            orders.setOrderTime(LocalDateTime.now());
            orders.setCheckoutTime(LocalDateTime.now());
            orders.setStatus(2);
            orders.setAmount(new BigDecimal(amont.get()));
            orders.setUserId(userId);
            orders.setUserName(user.getName());
            orders.setConsignee(addressBook.getConsignee());
            orders.setPhone(addressBook.getPhone());
            orders.setAddress(addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName()
                    + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                    + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                    + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));


            orderDetailService.saveBatch(orderDetails);
            shoppingCartService.remove(wrapper);
        } catch (CustoException e) {
            e.printStackTrace();
            transactionManager.rollback(status);
        }
    }
}

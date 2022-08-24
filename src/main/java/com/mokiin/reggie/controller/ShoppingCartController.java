package com.mokiin.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mokiin.reggie.conmmon.BaseContext;
import com.mokiin.reggie.conmmon.Result;
import com.mokiin.reggie.pojo.ShoppingCart;
import com.mokiin.reggie.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author mokiin
 * @since 2022-08-23
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        // 如果不为空说明是菜品
        if (null != shoppingCart.getDishId()) {
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            // 添加到购物车的是套餐
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartDb = shoppingCartService.getOne(wrapper);
        // 为空表示购物车没有该商品
        if (null == shoppingCartDb) {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCartDb = shoppingCart;
            // 购物车有该商品
        } else {
            shoppingCartDb.setNumber(shoppingCartDb.getNumber() + 1);
            shoppingCartService.updateById(shoppingCartDb);
        }
        return Result.ok(shoppingCartDb);
    }

    @GetMapping("/list")
    public Result list(ShoppingCart shoppingCart) {
        shoppingCart.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != shoppingCart.getUserId(), ShoppingCart::getUserId, shoppingCart.getUserId());
        wrapper.orderByAsc(ShoppingCart::getCreateTime);
        return Result.ok(shoppingCartService.list(wrapper));
    }

    @DeleteMapping("/clean")
    public Result clean() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(wrapper);
        return Result.ok();
    }
}


package com.mokiin.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mokiin.reggie.conmmon.CustoException;
import com.mokiin.reggie.mapper.CategoryMapper;
import com.mokiin.reggie.pojo.Category;
import com.mokiin.reggie.pojo.Dish;
import com.mokiin.reggie.pojo.Setmeal;
import com.mokiin.reggie.service.CategoryService;
import com.mokiin.reggie.service.DishService;
import com.mokiin.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Mokiin
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    @Override
    public void removeByIds(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询Dish表中的CategoryId和传进来的菜品id进行比较
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        // 查询当前分类是否关联了菜品，如果已经关联抛出业务异常
        if (dishCount > 0) {
            throw new CustoException("当前分类下关联了菜品，无法删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询setmea表中的CategoryId和传进来的菜品id进行比较
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        // 查询当前分类是否关联了套餐，如果已经关联抛出业务异常
        if (setmealCount > 0) {
            throw new CustoException("当前分类下关联了套餐，无法删除");
        }
        categoryMapper.deleteById(id);
    }
}

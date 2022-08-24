package com.mokiin.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mokiin.reggie.dto.DishDto;
import com.mokiin.reggie.mapper.DishMapper;
import com.mokiin.reggie.pojo.Dish;
import com.mokiin.reggie.pojo.DishFlavor;
import com.mokiin.reggie.service.DishFlavorService;
import com.mokiin.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author mokiin
 * @since 2022-08-21
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishMapper dishMapper;

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // 保存基本信息到dish
            dishMapper.insert(dishDto);
            // 菜品id
            Long dishId = dishDto.getId();
            List<DishFlavor> flavors = dishDto.getFlavors();
            flavors = flavors.stream().map(item -> {
                item.setDishId(dishId);
                return item;
            }).collect(Collectors.toList());
            // 菜品口味保存到dishFlavor
            dishFlavorService.saveBatch(dishDto.getFlavors());

        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
        }

    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 根据id查询菜品
        Dish dish = dishMapper.selectById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        // 条件构造器
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(wrapper);
        // 设置菜品对应的口味
        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 删除菜品
        dishMapper.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        // 删除关联的口味信息
        dishFlavorService.remove(wrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors =   flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}

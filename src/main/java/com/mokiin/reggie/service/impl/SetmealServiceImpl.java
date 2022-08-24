package com.mokiin.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mokiin.reggie.conmmon.CustoException;
import com.mokiin.reggie.dto.SetmealDto;
import com.mokiin.reggie.mapper.SetmealMapper;
import com.mokiin.reggie.pojo.Setmeal;
import com.mokiin.reggie.pojo.SetmealDish;
import com.mokiin.reggie.service.SetmealDishService;
import com.mokiin.reggie.service.SetmealService;
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
 * 套餐 服务实现类
 * </p>
 *
 * @author mokiin
 * @since 2022-08-21
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealMapper setmealMapper;

    @Resource
    private SetmealDishService setmealDishService;

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
            // 保存套餐基本信息
            setmealMapper.insert(setmealDto);
            List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
            setmealDishes = setmealDishes.stream().map(item -> {
                item.setSetmealId(setmealDto.getId());
                return item;
            }).collect(Collectors.toList());

            setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        wrapper.eq(Setmeal::getStatus,1);

        Integer count = setmealMapper.selectCount(wrapper);
        if (count > 0) {
            throw new CustoException("商品正在售卖，无法删除");
        }
        // 删除套餐数据
        setmealMapper.deleteBatchIds(ids);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getDishId,ids);
        //删除关联菜品数据
        setmealDishService.remove(queryWrapper);
    }

    @Override
    public void updateStatus(Long id) {
//        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
//        wrapper.in(Setmeal::getId,ids);
//        List<Setmeal> setmeals = setmealMapper.selectList(wrapper);
//        for (Setmeal setmeal : setmeals) {
//            if (setmeal.getStatus() == 1) {
//                setmeal.setStatus(0);
//                return;
//            }
//
//            if (setmeal.getStatus() == 0) {
//                setmeal.setStatus(1);
//                return;
//            }
//        }
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getId,id);
        Setmeal setmeal = setmealMapper.selectById(id);
        if (setmeal.getStatus() == 0 ){
            setmeal.setStatus(1);
        }else{
            setmeal.setStatus(0);
        }
        setmealMapper.updateById(setmeal);
    }
}

package com.mokiin.reggie.service.impl;

import com.mokiin.reggie.dto.SetmealDto;
import com.mokiin.reggie.pojo.Setmeal;
import com.mokiin.reggie.mapper.SetmealMapper;
import com.mokiin.reggie.service.SetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Override
    public void saveWithDish(SetmealDto setmealDto) {

    }
}

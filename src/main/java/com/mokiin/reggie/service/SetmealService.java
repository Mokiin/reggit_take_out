package com.mokiin.reggie.service;

import com.mokiin.reggie.dto.SetmealDto;
import com.mokiin.reggie.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author mokiin
 * @since 2022-08-21
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);
}

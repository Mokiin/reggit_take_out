package com.mokiin.reggie.service;

import com.mokiin.reggie.dto.SetmealDto;
import com.mokiin.reggie.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    /**
     * 删除一个或多个套餐
     * @param ids
     */
    void removeWithDish(List<Long> ids);

    /**
     * 启用或者停用
     * @param id
     */
    void updateStatus(Long id);
}

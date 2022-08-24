package com.mokiin.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mokiin.reggie.dto.DishDto;
import com.mokiin.reggie.pojo.Dish;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author mokiin
 * @since 2022-08-21
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品同时保存口味数据
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品并更新口味信息
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);
}

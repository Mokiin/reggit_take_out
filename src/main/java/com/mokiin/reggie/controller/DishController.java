package com.mokiin.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mokiin.reggie.conmmon.Result;
import com.mokiin.reggie.dto.DishDto;
import com.mokiin.reggie.pojo.Category;
import com.mokiin.reggie.pojo.Dish;
import com.mokiin.reggie.pojo.DishFlavor;
import com.mokiin.reggie.service.CategoryService;
import com.mokiin.reggie.service.DishFlavorService;
import com.mokiin.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author mokiin
 * @since 2022-08-21
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    private DishService dishService;

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return Result.ok().message("新增菜品成功");
    }

    /**
     * 分页并且模糊查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result page(int page, int pageSize, String name) {
        // 分页构造器
        Page<Dish> buildPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        // 执行构造器
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        wrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        // 添加排序条件
        wrapper.orderByDesc(Dish::getUpdateTime);
        // 执行分页查询
        dishService.page(buildPage, wrapper);
        // 对象拷贝
        BeanUtils.copyProperties(buildPage, dishDtoPage, "records");
        List<Dish> records = buildPage.getRecords();
        List<DishDto> list = null;
        list = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            // 分类id
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            // 获取分类名称
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return Result.ok(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return null;
    }

    /**
     * 更新菜品并更新口味信息
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return Result.ok();
    }

    /**
     * 查询菜品数据
     *
     * @return
     */
    @GetMapping("/list")
    public Result list(Dish dish) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != dish.getCategoryId(), Dish::getCategoryId, dish.getCategoryId());
        // 状态为1表示售卖中
        wrapper.eq(Dish::getStatus, 1);
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishs = dishService.list(wrapper);
        List<DishDto> dishDtos = dishs.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, item.getId());
            dishDto.setFlavors(dishFlavorService.list(queryWrapper));
            return dishDto;
        }).collect(Collectors.toList());
        return Result.ok(dishDtos);

    }

}


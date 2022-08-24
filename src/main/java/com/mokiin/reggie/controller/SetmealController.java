package com.mokiin.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mokiin.reggie.conmmon.Result;
import com.mokiin.reggie.dto.SetmealDto;
import com.mokiin.reggie.pojo.Category;
import com.mokiin.reggie.pojo.Setmeal;
import com.mokiin.reggie.service.CategoryService;
import com.mokiin.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author mokiin
 * @since 2022-08-21
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    @Resource
    private CategoryService categoryService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return Result.ok();
    }

    ;

    /**
     * 分页显示套餐
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>();
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        // 模糊查询
        wrapper.like(null != name, Setmeal::getName, name);
        // 排序条件
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, wrapper);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (null != category) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return Result.ok(setmealDtoPage);
    }

    /**
     * 删除一个或多个套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return Result.ok();
    }

    ;

    /**
     * 套餐状态设置
     *
     * @param ids
     * @return
     */
    @PostMapping(value = {"/status/0", "/status/1"})
    public Result status(@RequestParam("ids") Long[] ids) {
        for (Long id : ids) {
            setmealService.updateStatus(id);
        }
        return Result.ok();
    }

    @GetMapping("/list")
    public Result list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != setmeal.getCategoryId(), Setmeal::getCategoryId, setmeal.getCategoryId());
        wrapper.eq(null != setmeal.getStatus(), Setmeal::getStatus, setmeal.getStatus());
        List<Setmeal> list = setmealService.list(wrapper);
        return Result.ok(list);
    }
}


package com.mokiin.reggie.controller;


import com.mokiin.reggie.conmmon.Result;
import com.mokiin.reggie.dto.SetmealDto;
import com.mokiin.reggie.service.SetmealService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return Result.ok();
    }

}


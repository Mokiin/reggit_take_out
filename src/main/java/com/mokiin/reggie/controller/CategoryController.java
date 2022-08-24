package com.mokiin.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mokiin.reggie.conmmon.Result;
import com.mokiin.reggie.pojo.Category;
import com.mokiin.reggie.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Mokiin
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result page(int page, int pageSize) {
        Page<Category> pageModel = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        categoryService.page(pageModel, wrapper);
        return Result.ok(pageModel);
    }

    /**
     * 添加分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Category category) {
        categoryService.save(category);
        return Result.ok().message("新增分类成功");
    }

    /**
     * 删除分类
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result deleteById(Long ids) {
        categoryService.removeByIds(ids);
        return Result.ok().message("删除分类成功");
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping
    public Result update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.ok(category);
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result list(Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType() != null,Category::getType,category.getType());
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(wrapper);
        return Result.ok(list);
    }

}

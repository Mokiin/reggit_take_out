package com.mokiin.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mokiin.reggie.pojo.Category;
import com.mokiin.reggie.pojo.Employee;
import org.springframework.stereotype.Service;

/**
 * @author Mokiin
 */
@Service
public interface CategoryService extends IService<Category> {

    /**
     * 删除菜品分类
     * @param id
     */
    void removeByIds(Long id);

}

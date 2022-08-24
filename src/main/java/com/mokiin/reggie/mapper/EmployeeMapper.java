package com.mokiin.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mokiin.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Mokiin
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

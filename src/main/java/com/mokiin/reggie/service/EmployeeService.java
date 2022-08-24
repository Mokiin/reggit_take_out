package com.mokiin.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mokiin.reggie.pojo.Employee;
import org.springframework.stereotype.Service;

/**
 * @author Mokiin
 */
@Service
public interface EmployeeService extends IService<Employee> {
    /**
     * 登录
     * @param employee
     */
    Employee login(Employee employee);
}

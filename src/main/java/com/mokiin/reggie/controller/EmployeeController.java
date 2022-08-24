package com.mokiin.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mokiin.reggie.conmmon.Result;
import com.mokiin.reggie.pojo.Employee;
import com.mokiin.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author Mokiin
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 登录方法
     *
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 将页面提交的密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        Employee emp = employeeService.login(employee);
        // 数据库没有对象返回false
        if (emp == null) {
            return Result.fail(emp).code(0).message("账户未注册");
        }
        // 密码对比
        if (!password.equals(emp.getPassword())) {
            return Result.fail(emp).code(0).message("密码错误");
        }
        // 查看员工状态是否禁用
        if (emp.getStatus() == 0) {
            return Result.fail(emp).code(0).message("账户被封禁");
        }
        request.getSession().setAttribute("employee", emp.getId());
        return Result.ok(emp).message("登录成功");
    }

    /**
     * 退出方法
     *
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        // 清楚session中保存的员工id
        request.getSession().removeAttribute("employee");
        return Result.ok().message("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public Result save(HttpServletRequest request, @RequestBody Employee employee) {
        // 设置初始面膜并进行加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        // 设置创建时间和修改修改时间
       // employee.setCreateTime(LocalDateTime.now());
       // employee.setUpdateTime(LocalDateTime.now());
        // 获取当前操作的用户id
        Long empId = (Long) request.getSession().getAttribute("employee");
        // 设置创建和修改该用户的id
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return Result.ok("新增员工成功");
    }

    /**
     * 分页查询，模糊查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result page(int page, int pageSize, String name) {
        // 构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件，只有name不为空才执行
        wrapper.like(StringUtils.isNotEmpty(name), Employee::getUsername, name);
        // 添加排序条件
        wrapper.orderByDesc(Employee::getUpdateTime);
        // 执行查询
        employeeService.page(pageInfo, wrapper);
        return Result.ok(pageInfo);
    }

    /**
     * 根据id修改
     *
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public Result update(HttpServletRequest request, @RequestBody Employee employee) {
        // 修改该信息的时间
       // employee.setUpdateTime(LocalDateTime.now());
        // 修改该信息的用户
        Long empId = (Long) request.getSession().getAttribute("employee");
        log.info("empId : {}",empId);
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return Result.ok().message("修改成功");
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return Result.ok(employee);
        }
        return Result.fail().message("没有该用户信息");
    }
}

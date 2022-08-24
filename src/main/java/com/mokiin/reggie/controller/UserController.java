package com.mokiin.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mokiin.reggie.conmmon.Result;
import com.mokiin.reggie.pojo.User;
import com.mokiin.reggie.service.UserService;
import com.mokiin.reggie.utils.SMSUtils;
import com.mokiin.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author mokiin
 * @since 2022-08-23
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/sendMsg")
    public Result sendMsg(@RequestBody User user, HttpSession session) {
        // 先获取手机号
        String phone = user.getPhone();
        // 判断是否为空
        if (null != phone) {
            // 调用工具类获取随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码:{}",code);
            // 调用阿里云api
           // SMSUtils.sendMessage("","", phone,code);
            // 将生成的验证码保存到session
            session.setAttribute(phone,code);
            return Result.ok();
        }
        return Result.fail();
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map map, HttpSession request) {
        // 获取手机号
        String phone = map.get("phone").toString();
//        if (null == phone) {
//            return Result.fail().message("请输入手机号");
//        }
        // 获取验证码
        String code = map.get("code").toString();
//        if (null == code) {
//            return Result.fail().message("请输入验证码");
//        }
        // 从session获取保存的验证码
        Object codeInSession = request.getAttribute(phone);
        // 验证码比对
        if (code.equals(codeInSession)) {
            // 比对成功，登录
            // 手机号如未注册，先注册
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone,phone);
            User user = userService.getOne(wrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            request.setAttribute("user",user.getId() );
            return Result.ok(user);
        }

        return Result.fail();
    }

}


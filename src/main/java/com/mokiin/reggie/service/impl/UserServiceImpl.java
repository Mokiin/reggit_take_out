package com.mokiin.reggie.service.impl;

import com.mokiin.reggie.pojo.User;
import com.mokiin.reggie.mapper.UserMapper;
import com.mokiin.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author mokiin
 * @since 2022-08-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

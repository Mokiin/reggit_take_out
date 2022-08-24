package com.mokiin.reggie.service.impl;

import com.mokiin.reggie.pojo.AddressBook;
import com.mokiin.reggie.mapper.AddressBookMapper;
import com.mokiin.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author mokiin
 * @since 2022-08-23
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}

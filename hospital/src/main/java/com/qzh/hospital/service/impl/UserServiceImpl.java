package com.qzh.hospital.service.impl;

import com.qzh.hospital.entity.User;
import com.qzh.hospital.mapper.UserMapper;
import com.qzh.hospital.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qiuzihan
 * @since 2024-06-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

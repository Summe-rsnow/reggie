package com.ale.reggie.service.impl;

import com.ale.reggie.domain.User;
import com.ale.reggie.mapper.UserMapper;
import com.ale.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

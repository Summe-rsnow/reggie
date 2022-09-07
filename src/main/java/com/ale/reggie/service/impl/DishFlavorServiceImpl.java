package com.ale.reggie.service.impl;

import com.ale.reggie.domain.DishFlavor;
import com.ale.reggie.mapper.DishFlavorMapper;
import com.ale.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}

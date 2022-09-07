package com.ale.reggie.service;

import com.ale.reggie.domain.Dish;
import com.ale.reggie.dto.DishDto;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    void updateWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    DishDto getByIdWithFlavor(Long id);

}

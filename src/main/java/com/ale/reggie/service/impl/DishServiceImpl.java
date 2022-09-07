package com.ale.reggie.service.impl;

import com.ale.reggie.domain.Dish;
import com.ale.reggie.domain.DishFlavor;
import com.ale.reggie.dto.DishDto;
import com.ale.reggie.mapper.DishMapper;
import com.ale.reggie.service.DishFlavorService;
import com.ale.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();

        //将list集合封装好菜品id
        List<DishFlavor> flavors =
                dishDto.getFlavors().stream()
                        .peek((item) -> item.setDishId(id))
                        .collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        Long id = dishDto.getId();

        //清理口味信息
        LambdaQueryWrapper<DishFlavor> queryChainWrapper = new LambdaQueryWrapper<>();
        queryChainWrapper.eq(DishFlavor::getDishId, id);
        dishFlavorService.remove(queryChainWrapper);

        //插入新的数据,将list集合封装好菜品id
        List<DishFlavor> flavors =
                dishDto.getFlavors().stream()
                        .peek((item) -> item.setDishId(id))
                        .collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }


    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);

        return dishDto;
    }
}

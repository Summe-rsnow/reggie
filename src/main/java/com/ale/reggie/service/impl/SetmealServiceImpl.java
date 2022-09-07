package com.ale.reggie.service.impl;

import com.ale.reggie.common.CustomException;
import com.ale.reggie.domain.Setmeal;
import com.ale.reggie.domain.SetmealDish;
import com.ale.reggie.dto.SetmealDto;
import com.ale.reggie.mapper.SetmealMapper;
import com.ale.reggie.service.SetmealDishService;
import com.ale.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);

        //保存套餐和菜品的关联信息
        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishes =
                setmealDto.getSetmealDishes().stream()
                        .peek((item) -> item.setSetmealId(id))
                        .collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐，确认是否能够删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        long count = this.count(queryWrapper);
        if (count>0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //可以删除，先删除套餐表中的数据--setmeal
        this.removeByIds(ids);

        //删除关系表中的数据--setmeal_dish
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper1);
    }
}


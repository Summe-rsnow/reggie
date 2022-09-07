package com.ale.reggie.service.impl;

import com.ale.reggie.common.CustomException;
import com.ale.reggie.domain.Category;
import com.ale.reggie.domain.Dish;
import com.ale.reggie.domain.Setmeal;
import com.ale.reggie.mapper.CategoryMapper;
import com.ale.reggie.service.CategoryService;
import com.ale.reggie.service.DishService;
import com.ale.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    //根据id删除
    @Override
    @Transactional
    public void removeById(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //判断分类是否关联了菜品，如已关联，抛出一个业务异常
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        if (dishService.count(dishLambdaQueryWrapper) > 0) {
            throw new CustomException("当前分类关联了菜品，不能删除");
        }

        //判断分类是否关联了套餐，如已关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        if (setmealService.count(setmealLambdaQueryWrapper) > 0) {
            throw new CustomException("当前分类关联了套餐，不能删除");
        }

        //正常删除
        super.removeById(id);

    }
}

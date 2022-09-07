package com.ale.reggie.service;

import com.ale.reggie.domain.Setmeal;
import com.ale.reggie.dto.SetmealDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    //新增套餐 保存菜品和套餐的联系关系
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}

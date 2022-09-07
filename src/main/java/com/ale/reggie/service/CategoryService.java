package com.ale.reggie.service;

import com.ale.reggie.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {

    //根据id删除重写
    void removeById(Long id);
}

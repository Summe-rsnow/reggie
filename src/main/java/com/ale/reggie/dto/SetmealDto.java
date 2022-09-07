package com.ale.reggie.dto;

import com.ale.reggie.domain.Setmeal;
import com.ale.reggie.domain.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

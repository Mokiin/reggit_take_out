package com.mokiin.reggie.dto;

import com.mokiin.reggie.pojo.Setmeal;
import com.mokiin.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

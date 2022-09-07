package com.ale.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

//全局异常处理，根据注解来拦截
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//处理异常注解
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        log.error(message);

        if (message.contains("Duplicate entry")) {//错误信息包含 “双重输入” 即账号重复
            String[] split = message.split(" ");//将错误信息已空格分割 第三个数据即为重复的账号
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)//处理异常注解
    public R<String> exceptionHandler(CustomException ex) {
        String message = ex.getMessage();
        log.error(message);
        return R.error(message);
    }
}

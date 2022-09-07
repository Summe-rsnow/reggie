package com.ale.reggie.service.impl;

import com.ale.reggie.domain.Employee;
import com.ale.reggie.mapper.EmployeeMapper;
import com.ale.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

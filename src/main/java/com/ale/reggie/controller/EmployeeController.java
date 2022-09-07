package com.ale.reggie.controller;

import com.ale.reggie.common.R;
import com.ale.reggie.domain.Employee;
import com.ale.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(value = "/employee", produces = "application/json; charset=utf-8")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //从数据库查询对象
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp == null) {//判断用户名是否存在
            return R.error("用户名不存在，请重试");
        }
        //将密码进行MD5加密
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        if (!emp.getPassword().equals(password)) {//判断密码是否正确
            return R.error("密码错误，请重试");
        }
        if (emp.getStatus() == 0) {//判断账户是否可用  0为不可用  1为可用
            return R.error("账户已禁用，请重试");
        }
        request.getSession().setAttribute("employee", emp.getId());//在request域中存储用户id 表示为登录状态
        return R.success(emp);//返回查到对象（以json格式）
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");//在request域中去除用户id 表示为未登录状态
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(@RequestBody Employee employee) {


        //MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        log.info("新增员工信息：{}", employee);
        /*
        创建与更新时间
        LocalDateTime now = LocalDateTime.now();
        employee.setCreateTime(now);
        employee.setUpdateTime(now);
        创建员工的人的ID
        long empID = (long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empID);
        employee.setUpdateUser(empID);
        */

        //执行保存操作 当有异常抛出会被common包下的GlobalExceptionHandler类进行异常处理
        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {

        //构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name); //like方法 ：Employee的name与传入的参数name有部分相同就可以匹配
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee) {

/*
        employee.setUpdateTime(LocalDateTime.now());
        long employeeId = (long) request.getSession().getAttribute("employee");
        employee.setUpdateUser(employeeId);
*/
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应用户");
    }
}


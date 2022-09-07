package com.ale.reggie.controller;

import com.ale.reggie.common.R;
import com.ale.reggie.domain.User;
import com.ale.reggie.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (phone != null && phone.length() > 0) {
//            生成验证码 （可以申请阿里云的短信服务进行发送，这里写死1234模拟生成的验证码）
//            String s = ValidateCodeUtils.generateValidateCode(4).toString(); //通过此方法可以生成验证码，传入参数为验证码长度
//            log.info("生成验证码为:{}",s);
            String code = "1234";
            //发送验证码（可以申请阿里云的短信服务进行发送）
            //SMSUtils.sendMessage("瑞吉外卖", "", phone, code); //如果购买了服务可以通过此方法发送验证码
            session.setAttribute(phone, code);
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object codeInSession = session.getAttribute(phone);
        //输入的验证码如果正确
        if (code.equals(codeInSession)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            //新用户自动完成注册
            if (user == null){
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }
}

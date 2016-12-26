package com.donglu.controller;

import com.donglu.bean.NoSecurity;
import com.donglu.bean.Response;
import com.donglu.bean.SystemAccount;
import com.donglu.config.SecurityInterceptor;
import com.donglu.mapper.LoginUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@RestController
@RequestMapping("/admin")
public class LoginUserController {

    @Autowired
    private LoginUserMapper loginUserMapper;

    @Autowired
    private SecurityInterceptor securityInterceptor;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @NoSecurity
    public Response login(@RequestParam String accountName, @RequestParam String accountPassword){
        SystemAccount loginUser = loginUserMapper.findOne(accountName);
        if (loginUser == null || !loginUser.getAccountPassword().equals(accountPassword)) {
            return new Response().failureMsg("用户名或密码错误");
        }
        loginUser.setAccountPassword(null);
        securityInterceptor.loginSession(loginUser);
        return new Response().successMsg("登录成功");
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public Response login(){
        Object login = securityInterceptor.getLogin();
        if (login == null) {
            return new Response().failureMsg("请重新登录");
        }
        return new Response().success(login);
    }

    @NoSecurity
    @RequestMapping(value = "/loginOut",method = RequestMethod.GET)
    public Response loginOut(){
        securityInterceptor.loginOut();
        return new Response().success();
    }

}

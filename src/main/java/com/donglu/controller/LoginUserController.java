package com.donglu.controller;

import com.donglu.bean.NoSecurity;
import com.donglu.bean.Response;
import com.donglu.bean.SystemAccount;
import com.donglu.config.SecurityInterceptor;
import com.donglu.mapper.LoginUserMapper;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

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
            return new Response().failure("用户名或密码错误");
        }
        loginUser.setAccountPassword(null);
        securityInterceptor.loginSesseion(loginUser);
        return new Response().success();
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public Response login(){
        SystemAccount systemAccount = (SystemAccount) securityInterceptor.getLogin();
        return new Response().success(systemAccount);
    }
}

package com.donglu.controller;

import com.donglu.bean.*;
import com.donglu.config.SecurityInterceptor;
import com.donglu.mapper.LoginUserMapper;
import com.donglu.mapper.VisitorControlMapper;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 访客控制器
 * Created by panmingzhi on 2016/11/27 0027.
 */
@RestController
@RequestMapping("/visitorController")
public class VisitorController {

    @Autowired
    private LoginUserMapper loginUserMapper;
    @Autowired
    private VisitorControlMapper visitorControlMapper;
    @Autowired
    private SecurityInterceptor securityInterceptor;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @NoSecurity
    public Response login(@RequestParam String accountName, @RequestParam String accountPassword){
        CardUser cardUser = loginUserMapper.findCardUser(accountName);
        if (cardUser == null) {
            return new Response().failureMsg("用户名或密码错误");
        }
        if (cardUser.getUserPassword() == null && accountPassword.length() != 6) {
            return new Response().failureMsg("用户名或密码错误");
        }
        if (cardUser.getUserPassword() == null && !accountName.endsWith(accountPassword)) {
            return new Response().failureMsg("用户名或密码错误");
        }
        if (cardUser.getUserPassword() != null && !cardUser.getUserPassword().equals(accountPassword)) {
            return new Response().failureMsg("用户名或密码错误");
        }
        cardUser.setUserPassword(null);
        securityInterceptor.loginSesseion(cardUser);
        return new Response().success();
    }

    @RequestMapping(value = "/visitor",method = RequestMethod.GET)
    public Response findVisitorList(@RequestParam String tableParam, @RequestParam String searchParam){
        DataTablePageUtil mapDataTablePageUtil = new DataTablePageUtil(tableParam,searchParam);
        PageHelper.offsetPage(mapDataTablePageUtil.getStart(),mapDataTablePageUtil.getLength(),false);
        PageHelper.orderBy(mapDataTablePageUtil.getsortStr());

        List<VisitorBooking> visitorBookList = visitorControlMapper.findVisitorBookList(mapDataTablePageUtil.getConditionMap());
        Long count = visitorControlMapper.countVisitorBookList(mapDataTablePageUtil.getConditionMap());
        return new ResponseTableList(count, visitorBookList);
    }

    @RequestMapping(value = "/visitor",method = RequestMethod.POST)
    public Response insertVisitorBooking(VisitorBooking visitorBooking) {
        return insertOrUpdateVisitorBooking(visitorBooking);
    }

    @RequestMapping(value = "/visitor",method = RequestMethod.PUT)
    public Response updateVisitorBooking(VisitorBooking visitorBooking) {
        return insertOrUpdateVisitorBooking(visitorBooking);
    }

    @RequestMapping(value = "/visitor/{id}",method = RequestMethod.DELETE)
    public Response deleteVisitorBooking(@PathVariable Long id) {
        try {
            Long count = visitorControlMapper.deleteVisitorBooking(id);
            if (count > 0) {
                return new Response().successMsg("删除成功");
            }else {
                return new Response().failureMsg("删除失败");
            }
        } catch (Exception e) {
            return new Response().failureMsg("删除失败:" + e.getMessage());
        }
    }

    private Response insertOrUpdateVisitorBooking(VisitorBooking visitorBooking) {
        Object obj = securityInterceptor.getLogin();
        if (!(obj instanceof CardUser)) {
            return new Response().failureMsg("请重新登录");
        }

        CardUser cardUser = (CardUser) obj;
        visitorBooking.copyFromCardUser(cardUser);
        if (visitorBooking.getId() == null) {
            visitorControlMapper.insert(visitorBooking);
            return new Response().successMsg("添加成功");
        } else {
            visitorControlMapper.update(visitorBooking);
            return new Response().successMsg("修改成功");
        }

    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}

package com.donglu.controller;

import com.donglu.bean.*;
import com.donglu.config.SecurityInterceptor;
import com.donglu.mapper.LoginUserMapper;
import com.donglu.mapper.VisitorControlMapper;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        if (Strings.isNullOrEmpty(accountName) || Strings.isNullOrEmpty(accountPassword)) {
            return new Response().failureMsg("用户名与密码不可为空");
        }
        CardUser cardUser = loginUserMapper.findCardUser(accountName);
        Optional<Response> response = checkLogin(cardUser, accountPassword);
        if (response.isPresent()) {
            return response.get();
        }
        cardUser.setUserPassword(null);
        securityInterceptor.loginSession(cardUser);
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

    @RequestMapping(value = "/login",method = RequestMethod.PUT)
    @NoSecurity
    public Response modifyPassword(@RequestParam String loginName, @RequestParam String loginPassword,@RequestParam String newPassword){
        if (Strings.isNullOrEmpty(loginName) || Strings.isNullOrEmpty(loginPassword) || Strings.isNullOrEmpty(newPassword)) {
            return new Response().failureMsg("用户名与密码不可为空");
        }
        CardUser cardUser = loginUserMapper.findCardUser(loginName);
        Optional<Response> response = checkLogin(cardUser, loginPassword);
        if (response.isPresent()) {
            return response.get();
        }
        loginUserMapper.updateCardUserPassword(loginName,newPassword);
        securityInterceptor.loginOut();
        return new Response().successMsg("修改成功，请重新登录");
    }

    public Optional<Response> checkLogin(CardUser cardUser, String loginPassword) {
        if (cardUser == null) {
            return Optional.of(new Response().failureMsg("用户名或密码错误"));
        }
        if (cardUser.getUserPassword() == null && loginPassword.length() != 6) {
            return Optional.of(new Response().failureMsg("用户名或密码错误"));
        }
        if (cardUser.getUserPassword() == null && !loginPassword.endsWith(loginPassword)) {
            return Optional.of(new Response().failureMsg("用户名或密码错误"));
        }
        if (cardUser.getUserPassword() != null && !cardUser.getUserPassword().equals(loginPassword)) {
            return Optional.of(new Response().failureMsg("用户名或密码错误"));
        }
        return Optional.empty();
    }

    @RequestMapping(value = "/visitor",method = RequestMethod.GET)
    public Response findVisitorList(@RequestParam String tableParam, @RequestParam String searchParam){
        DataTablePageUtil mapDataTablePageUtil = new DataTablePageUtil(tableParam,searchParam);
        PageHelper.offsetPage(mapDataTablePageUtil.getStart(),mapDataTablePageUtil.getLength(),false);
        PageHelper.orderBy(mapDataTablePageUtil.getsortStr());

        Map<String, Object> conditionMap = mapDataTablePageUtil.getConditionMap();
        CardUser cardUser = (CardUser) securityInterceptor.getLogin();
        conditionMap.put("userName", cardUser.getUserName());

        Optional.ofNullable(conditionMap.get("visitorTime")).ifPresent(s->{
            String visitorTime = (String) s;
            String[] split = visitorTime.split(" - ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startTime = LocalDate.parse(split[0].trim()).atTime(0, 0, 0).format(formatter);
            String endTime = LocalDate.parse(split[1].trim()).atTime(23, 58, 59).format(formatter);
            conditionMap.put("visitorStartTime",startTime);
            conditionMap.put("visitorEndTime",endTime);
        });

        List<VisitorBooking> visitorBookList = visitorControlMapper.findVisitorBookList(conditionMap);
        Long count = visitorControlMapper.countVisitorBookList(conditionMap);
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
            VisitorBooking visitor = visitorControlMapper.findVisitorBooking(id);
            if (visitor != null && "已完成".equals(visitor.getStatus())) {
                return new Response().failureMsg("己完成的预约不能再进行删除");
            }

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

//        visitorBooking.setVisitorTime(visitorBooking.getVisitorTime().);
        if (visitorBooking.getId() == null) {
            visitorBooking.setStatus("未完成");
            visitorControlMapper.insert(visitorBooking);
            return new Response().successMsg("添加成功");
        } else {
            VisitorBooking visitor = visitorControlMapper.findVisitorBooking(visitorBooking.getId());
            if (visitor != null && "已完成".equals(visitor.getStatus())) {
                return new Response().failureMsg("己完成的预约不能再进行修改");
            }

            visitorBooking.setStatus("未完成");
            visitorControlMapper.update(visitorBooking);
            return new Response().successMsg("修改成功");
        }

    }

}

package com.donglu.controller;

import com.donglu.bean.*;
import com.donglu.config.SecurityInterceptor;
import com.donglu.mapper.LoginUserMapper;
import com.donglu.mapper.VisitorControlMapper;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 访客控制器
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Api(value = "访客系统")
@RestController
@RequestMapping("/visitorController")
public class VisitorController {
    private static Logger LOGGER = LoggerFactory.getLogger(VisitorController.class);
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

    @NoSecurity
    @ApiOperation(value = "二维码验证",notes = "验证局域网访客二维码的可用性")
    @RequestMapping(value = "/visitor/code2",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
    public String validateCode2(@RequestParam String validData,@RequestParam String devSn){
        if (getValidResult(validData, devSn)) {
            return "{'ret':'OK','count':1}";
        }else {
            return "{'ret':'NO'}";
        }
    }
    @NoSecurity
    @ApiOperation(value = "二维码验证",notes = "验证互联网访客二维码的可用性")
    @RequestMapping(value = "/visitor/code1",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
    public String validateCode1(@RequestParam String qrcodeData,@RequestParam String devSn){
        if (getValidResult(qrcodeData, devSn)) {
            return "{'ret':0,'count':1}";
        }else {
            return "{'ret':0,'count':0}";
        }
    }

    private boolean getValidResult(@RequestParam String qrcodeData, @RequestParam String devSn) {
        LOGGER.info("二维验证：{} 设备：{}",qrcodeData,devSn);
        Long integer = Long.valueOf(qrcodeData);
        Visitor visitor = visitorControlMapper.findVisitor(integer);
        if (visitor == null) {
            LOGGER.warn("未打到二维码对应的访客信息,不允许出入");
            return false;
        }
        if (visitor.getOutTime() != null) {
            LOGGER.warn("访客：{}己注销，不允许再次进行二维码验证",visitor.getName());
            return false;
        }
        if (visitor.getInTime() == null) {
            LOGGER.warn("访客：{} 入场时间未设置，不允许再次进行二维码验证",visitor.getName());
            return false;
        }
        LocalDateTime inDateTime = LocalDateTime.ofInstant(visitor.getInTime().toInstant(), ZoneId.systemDefault());
        LocalDateTime firstTime = LocalDateTime.of(inDateTime.getYear(), inDateTime.getMonth(), inDateTime.getDayOfMonth(), 0, 0, 0);
        LocalDateTime lastTime = LocalDateTime.of(inDateTime.getYear(), inDateTime.getMonth(), inDateTime.getDayOfMonth(), 23, 59, 59);
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(firstTime)) {
            LOGGER.warn("访客{}验证未通过，系统有效时间段{}至{}，验证时间：{}",visitor.getName(),firstTime,lastTime,now);
            return false;
        }
        if (now.isAfter(lastTime)){
            LOGGER.warn("访客{}验证未通过，系统有效时间段{}至{}，验证时间：{}",visitor.getName(),firstTime,lastTime,now);
            return false;
        }

        LOGGER.warn("访客:{}验证通过，系统有效时间段{}至{}，验证时间：{}",visitor.getName(),firstTime,lastTime,now);
        return true;
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

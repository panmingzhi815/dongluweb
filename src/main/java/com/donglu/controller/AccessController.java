package com.donglu.controller;

import com.donglu.bean.*;
import com.donglu.mapper.AccessControlMapper;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@RestController
@RequestMapping("/accessControl")
public class AccessController {

    @Autowired
    private AccessControlMapper accessControlMapper;

    @RequestMapping(value = "/lastCardUsage",method = RequestMethod.GET)
    public Response lastCardUsage(@RequestParam(required = false) String deviceIdentifier){
        PageHelper.offsetPage(0,1,false);
        PageHelper.orderBy("CardUsage.timestamp desc");

        HashMap<String, String> map = Maps.newHashMap();
        map.put("deviceIdentifier",deviceIdentifier);
        CardUsage usage = accessControlMapper.findLastCardUsage(map);
        return new Response().success(usage);
    }

    @RequestMapping(value = "/cardUsage",method = RequestMethod.GET)
    public Response findLastCardUsage(@RequestParam String tableParam,@RequestParam String searchParam){
        DataTablePageUtil mapDataTablePageUtil = new DataTablePageUtil(tableParam,searchParam);
        PageHelper.offsetPage(mapDataTablePageUtil.getStart(),mapDataTablePageUtil.getLength(),false);
        PageHelper.orderBy(mapDataTablePageUtil.getsortStr());

        List<CardUsage> cardUsageList = accessControlMapper.findCardUsage(mapDataTablePageUtil.getConditionMap());
        Long count = accessControlMapper.countCardUsage(mapDataTablePageUtil.getConditionMap());
        return new ResponseTableList(count, cardUsageList);
    }

    @RequestMapping(value = "/accessControlRecord",method = RequestMethod.GET)
    public Response findAccessControlRecord(@RequestParam String tableParam,@RequestParam String searchParam) {
        DataTablePageUtil mapDataTablePageUtil = new DataTablePageUtil(tableParam,searchParam);
        PageHelper.offsetPage(mapDataTablePageUtil.getStart(),mapDataTablePageUtil.getLength(),false);
        PageHelper.orderBy(mapDataTablePageUtil.getsortStr());

        List<AccessControlRecord> cardUsageList = accessControlMapper.findAccessControlRecord(mapDataTablePageUtil.getConditionMap());
        Long count = accessControlMapper.countAccessControlRecord(mapDataTablePageUtil.getConditionMap());
        return new ResponseTableList(count, cardUsageList);
    }

    @RequestMapping(value = "/cardUser",method = RequestMethod.GET)
    public Response findCardUser(@RequestParam String tableParam,@RequestParam String searchParam){
        DataTablePageUtil mapDataTablePageUtil = new DataTablePageUtil(tableParam,searchParam);
        PageHelper.offsetPage(mapDataTablePageUtil.getStart(),mapDataTablePageUtil.getLength(),false);
        PageHelper.orderBy(mapDataTablePageUtil.getsortStr());

        List<CardUser> cardUsageList = accessControlMapper.findCardUser(mapDataTablePageUtil.getConditionMap());
        Long count = accessControlMapper.countCardUser(mapDataTablePageUtil.getConditionMap());
        return new ResponseTableList(count, cardUsageList);
    }

    @RequestMapping(value = "/device",method = RequestMethod.GET)
    public Response findDevice(@RequestParam(required = false) String tableParam,@RequestParam(required = false) String searchParam){
        DataTablePageUtil mapDataTablePageUtil = new DataTablePageUtil(tableParam,searchParam);
        PageHelper.offsetPage(0,Integer.MAX_VALUE,false);
        PageHelper.orderBy(mapDataTablePageUtil.getsortStr());

        List<Device> deviceList = accessControlMapper.findDevice(mapDataTablePageUtil.getConditionMap());
        return new Response().success(deviceList);
    }

    @RequestMapping(value = "/enum",method = RequestMethod.GET)
    public Response findAccessControlState(@RequestParam String enumType){
        switch (enumType) {
            case "AccessControlState":
                return new Response().success(getJsonObjects(AccessControlState.values()));
            case "CardStatusEnum":
                return new Response().success(getJsonObjects(CardStatusEnum.values()));
            case "EventTypeEnum":
                return new Response().success(getJsonObjects(EventTypeEnum.values()));
            default:
                return new Response().failureMsg("未找到对应的枚举：" + enumType);
        }
    }

    private List<Map<String,Object>> getJsonObjects(Enum[] values) {
        return Arrays.asList(values).stream().map(m -> {
            HashMap<String, Object> map = Maps.newHashMap();
            map.put("name", m.name());
            map.put("value",m.ordinal());
            return map;
        }).collect(Collectors.toList());
    }
}

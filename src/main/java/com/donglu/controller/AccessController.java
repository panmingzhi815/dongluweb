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
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@RestController
@RequestMapping("/accessControl")
public class AccessController {

    @Autowired
    private AccessControlMapper accessControlMapper;

    @RequestMapping(value = "/cardUsage",method = RequestMethod.GET)
    public Response findCardUsage(@RequestParam String tableParam,@RequestParam String searchParam){
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

}

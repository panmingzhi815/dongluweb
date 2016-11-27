package com.donglu.controller;

import com.donglu.bean.CardUsage;
import com.donglu.bean.Response;
import com.donglu.bean.ResponseTableList;
import com.donglu.mapper.AccessControlMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@RestController
@RequestMapping("/accessControl")
public class AccessController {

    @Autowired
    private AccessControlMapper accessControlMapper;

    @RequestMapping(value = "/cardUsage")
    public Response getCardUsageList(@RequestParam Integer start,@RequestParam Integer length){
        PageHelper.offsetPage(start,length);
        PageHelper.orderBy("id");
        List<CardUsage> cardUsageList = accessControlMapper.findCardUsage();
        Long count = accessControlMapper.countCardUsage();
        return new ResponseTableList(count, cardUsageList);
    }

}

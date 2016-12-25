package com.donglu.mapper;

import com.donglu.bean.AccessControlRecord;
import com.donglu.bean.CardUsage;
import com.donglu.bean.CardUser;
import com.donglu.bean.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Mapper
@Component
public interface AccessControlMapper {

    List<CardUsage> findCardUsage(Map<String, String> conditionMap);

    Long countCardUsage(Map<String, String> conditionMap);

    List<AccessControlRecord> findAccessControlRecord(Map<String,String> map);

    Long countAccessControlRecord(Map<String,String> map);

    List<CardUser> findCardUser(Map<String, String> conditionMap);

    Long countCardUser(Map<String, String> conditionMap);

    List<Device> findDevice(Map<String, String> conditionMap);

    CardUsage findLastCardUsage(Map<String,String> map);

}

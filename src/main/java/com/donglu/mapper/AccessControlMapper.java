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

    List<CardUsage> findCardUsage(Map<String, Object> conditionMap);

    Long countCardUsage(Map<String, Object> conditionMap);

    List<AccessControlRecord> findAccessControlRecord(Map<String,Object> map);

    Long countAccessControlRecord(Map<String,Object> map);

    List<CardUser> findCardUser(Map<String, Object> conditionMap);

    Long countCardUser(Map<String, Object> conditionMap);

    List<Device> findDevice(Map<String, Object> conditionMap);

    CardUsage findLastCardUsage(Map<String,Object> map);

}

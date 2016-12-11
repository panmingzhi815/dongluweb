package com.donglu.mapper;

import com.donglu.bean.AccessControlRecord;
import com.donglu.bean.CardUsage;
import com.donglu.bean.CardUser;
import com.donglu.bean.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Mapper
public interface AccessControlMapper {

//    @SelectProvider(type = AccessControlMapperProvider.class, method = "findCardUsage")
    List<CardUsage> findCardUsage(Map<String, String> conditionMap);

//    @SelectProvider(type = AccessControlMapperProvider.class, method = "countCardUsage")
    Long countCardUsage(Map<String, String> conditionMap);

//    @SelectProvider(type = AccessControlMapperProvider.class, method = "findAccessControlRecord")
    List<AccessControlRecord> findAccessControlRecord(Map<String,String> map);

//    @SelectProvider(type = AccessControlMapperProvider.class, method = "countAccessControlRecord")
    Long countAccessControlRecord(Map<String,String> map);

//    @SelectProvider(type = AccessControlMapperProvider.class, method = "findCardUser")
    List<CardUser> findCardUser(Map<String, String> conditionMap);

//    @SelectProvider(type = AccessControlMapperProvider.class, method = "countCardUser")
    Long countCardUser(Map<String, String> conditionMap);

//    @SelectProvider(type = AccessControlMapperProvider.class, method = "findDevice")
    List<Device> findDevice(Map<String, String> conditionMap);

//    @SelectProvider(type = AccessControlMapperProvider.class, method = "findLastCardUsage")
    CardUsage findLastCardUsage(Map<String,String> map);
}

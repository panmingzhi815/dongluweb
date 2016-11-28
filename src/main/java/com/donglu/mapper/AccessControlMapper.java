package com.donglu.mapper;

import com.donglu.bean.AccessControlRecord;
import com.donglu.bean.CardUsage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Mapper
public interface AccessControlMapper {

    @Select("select * from CardUsage")
    List<CardUsage> findCardUsage();

    @Select("select count(id) from CardUsage")
    Long countCardUsage();

    @Select("SELECT acr.id,acr.accessControlState,d.Identifier as deviceIdentifier,d.Name as deviceName,pc.PhysicalId as cardIdentifier,pc.SerialNumber as cardSerialNumber,cu.identifier as userIdentifier,cu.name as userName,acr.createTime,acr.checkCardEnabled,acr.checkValidPeriod,acr.enableOpenFromInside,acr.enableWeekTable,acr.validFrom,acr.validTo,acr.weektable,acr.uploadTime FROM AccessControlRecord acr LEFT JOIN PhysicalCard pc on acr.card_id = pc.id LEFT JOIN Device d on acr.device = d.id LEFT JOIN CardUser cu on pc.cardUser = cu.id")
    List<AccessControlRecord> findAccessControlRecord();

    @Select("select count(id) from AccessControlRecord")
    Long countAccessControlRecord();
}

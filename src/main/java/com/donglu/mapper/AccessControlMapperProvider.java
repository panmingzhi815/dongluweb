package com.donglu.mapper;

import com.donglu.bean.AccessControlState;
import com.google.common.base.Strings;
import net.sf.jsqlparser.util.SelectUtils;

import java.util.Map;

/**
 * Created by panmingzhi on 2016/11/29 0029.
 */
public class AccessControlMapperProvider {

    public String findLastCardUsage(String deviceIdentifier) {
        StringBuilder sql = new StringBuilder();

        return sql.toString();
    }

    public String findDevice(Map<String ,String> map){
        StringBuilder sql = new StringBuilder(200);
        sql.append("SELECT d.Identifier as deviceIdentifier,d.Name as deviceName FROM Device d where 1 = 1 ");

        String deviceIdentifier = map.get("deviceIdentifier");
        if (!Strings.isNullOrEmpty(deviceIdentifier)) {
            sql.append(" and d.Identifier = ").append(deviceIdentifier);
        }
        return sql.toString();
    }

    public String findCardUser(Map<String, String> map) {
        StringBuilder sql = new StringBuilder(200);
        sql.append("SELECT p.id AS cardId, p.PhysicalId AS cardIdentifier, p.serialNumber AS cardSerialNumber, p.Status AS cardStatusEnum, c.id AS userId, c.name AS userName, c.identifier AS userIdentifier, c.groupCodeNameJoinStr AS userGroup FROM PhysicalCard p FULL JOIN CardUser c ON p.cardUser = c.id where 1 = 1 ");
        return cardUser(sql,map);
    }

    public String countCardUser(Map<String, String> map) {
        StringBuilder sql = new StringBuilder(200);
        sql.append("select count(0) from (");
        sql.append(findCardUser(map));
        sql.append(") as t");
        return sql.toString();
    }

    private String cardUser(StringBuilder sql, Map<String, String> map) {
        String userName = map.get("userName");
        if (!Strings.isNullOrEmpty(userName)) {
            sql.append(" and c.name like '%").append(userName).append("%' ");
        }
        String userIdentifier = map.get("userIdentifier");
        if (!Strings.isNullOrEmpty(userIdentifier)) {
            sql.append(" and c.identifier like '%").append(userIdentifier).append("%' ");
        }
        String cardIdentifier = map.get("cardIdentifier");
        if (!Strings.isNullOrEmpty(cardIdentifier)) {
            sql.append(" and p.physicalId like '%").append(cardIdentifier).append("%' ");
        }
        String cardSerialNumber = map.get("cardSerialNumber");
        if (!Strings.isNullOrEmpty(cardSerialNumber)) {
            sql.append(" and p.serialNumber like '%").append(cardSerialNumber).append("%' ");
        }
        return sql.toString();
    }

    public String findCardUsage(Map<String,String> map){
        StringBuilder sql = new StringBuilder(200);
        sql.append("select id,cardid as cardIdentifier,cardSerialNumber,deviceIdentifier,deviceName,userIdentifier,userName,eventTime,timestamp,eventType from CardUsage where 1=1");
        return cardUsage(map, sql);
    }


    public String countCardUsage(Map<String,String> map){
        StringBuilder sql = new StringBuilder(200);
        sql.append("select count(0) from (");
        sql.append(findCardUsage(map));
        sql.append(") as t");
        return sql.toString();
    }

    private String cardUsage(Map<String, String> map, StringBuilder sql) {
        String userName = map.get("userName");
        if (!Strings.isNullOrEmpty(userName)) {
            sql.append(" and userName like '%").append(userName).append("%' ");
        }
        String deviceName = map.get("deviceName");
        if (!Strings.isNullOrEmpty(deviceName)) {
            sql.append(" and deviceName like '%").append(deviceName).append("%' ");
        }
        String cardIdentifier = map.get("cardIdentifier");
        if (!Strings.isNullOrEmpty(cardIdentifier)) {
            sql.append(" and cardid like '%").append(cardIdentifier).append("%' ");
        }
        String betweenTime = map.get("betweenTime");
        if (!Strings.isNullOrEmpty(betweenTime)) {
            String[] split = betweenTime.split(" - ");
            sql.append(" and eventTime between '").append(split[0]).append("' and '").append(split[1]).append("' ");
        }
        return sql.toString();
    }

    public String findAccessControlRecord(Map<String,String> map){
        StringBuilder stringBuffer = new StringBuilder(200);
        stringBuffer.append("SELECT acr.id as acrId,acr.accessControlState as accessControlState,d.Identifier as deviceIdentifier,d.Name as deviceName,pc.PhysicalId as cardIdentifier,pc.SerialNumber as cardSerialNumber,cu.identifier as userIdentifier,cu.name as userName,acr.createTime as createTime,acr.checkCardEnabled as checkCardEnabled,checkValidPeriod as checkValidPeriod,acr.enableOpenFromInside as enableOpenFromInside,acr.enableWeekTable as enableWeekTable,acr.validFrom as validFrom,acr.validTo as validTo,acr.weektable as weektable,acr.uploadTime as uploadTime FROM AccessControlRecord acr LEFT JOIN PhysicalCard pc on acr.card_id = pc.id LEFT JOIN Device d on acr.device = d.id LEFT JOIN CardUser cu on pc.cardUser = cu.id where 1= 1 ");
        accessControlRecord(map, stringBuffer);
        return stringBuffer.toString();
    }

    public String countAccessControlRecord(Map<String,String> map){
        StringBuilder stringBuffer = new StringBuilder(200);
        stringBuffer.append("SELECT count(1) FROM AccessControlRecord acr LEFT JOIN PhysicalCard pc on acr.card_id = pc.id LEFT JOIN Device d on acr.device = d.id LEFT JOIN CardUser cu on pc.cardUser = cu.id where 1= 1 ");
        accessControlRecord(map, stringBuffer);
        return stringBuffer.toString();
    }

    private void accessControlRecord(Map<String, String> map, StringBuilder stringBuffer) {
        String userName = map.get("userName");
        if (!Strings.isNullOrEmpty(userName)) {
            stringBuffer.append(" and cu.name like '%").append(userName).append("%' ");
        }
        String accessControlState = map.get("accessControlState");
        if (!Strings.isNullOrEmpty(accessControlState) && !"全部".equals(accessControlState)) {
            stringBuffer.append(" and acr.accessControlState = ").append(AccessControlState.valueOf(accessControlState).ordinal()).append(" ");
        }
        String deviceName = map.get("deviceName");
        if (!Strings.isNullOrEmpty(deviceName)) {
            stringBuffer.append(" and d.Name like '%").append(deviceName).append("%' ");
        }
        String cardIdentifier = map.get("cardIdentifier");
        if (!Strings.isNullOrEmpty(cardIdentifier)) {
            stringBuffer.append(" and pc.PhysicalId like '%").append(cardIdentifier).append("%' ");
        }
    }

}

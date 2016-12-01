package com.donglu.bean;

import lombok.Data;

import java.util.Date;

/**
 * *@author panmingzhi
 */

@Data
public class AccessControlRecord {
    private int acrId;
    private String deviceIdentifier;
    private String deviceName;
    private AccessControlState accessControlState;
    private String cardIdentifier;
    private String cardSerialNumber;
    private String userIdentifier;
    private String userName;
    private Date createTime;
    private Boolean checkCardEnabled;
    private Boolean checkValidPeriod;
    private Boolean enableOpenFromInside;
    private Boolean enableWeekTable;
    private Date validFrom;
    private Date validTo;
    private String weektable;
    private Date uploadTime;

}
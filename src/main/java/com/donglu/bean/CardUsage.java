package com.donglu.bean;

import lombok.Data;

import java.util.Date;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Data
public class CardUsage {
    private Long id;
    private String cardIdentifier;
    private String cardSerialNumber;
    private String deviceIdentifier;
    private String deviceName;
    private String userIdentifier;
    private String userName;
    private String userGroup;
    private Date eventTime;
    private Date timestamp;
    private EventTypeEnum eventType;
    private String imageName;
}

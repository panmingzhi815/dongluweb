package com.donglu.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 访客预约
 * Created by panmingzhi on 2016/12/23 0023.
 */
@Data
public class VisitorBooking implements Serializable {
    private Long id;
    @JsonProperty(value = "IDCard")
    private String IDCard;
    private String address;
    private Long cardUserId;
    private String companyName;
    private String operaName;
    private String phone;
    private String reason;
    private String sex;
    private String userGroup;
    private String userName;
    private String visitorName;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date visitorTime;

    public void copyFromCardUser(CardUser cardUser) {
        this.setUserName(cardUser.getUserName());
        this.setUserGroup(cardUser.getUserGroup());
        this.setCardUserId(cardUser.getUserId());
    }
}

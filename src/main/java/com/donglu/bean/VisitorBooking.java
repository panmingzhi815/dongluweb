package com.donglu.bean;

import com.donglu.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

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
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonFormat()
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso = DateTimeFormat.ISO.DATE)
    private LocalDate visitorTime;
    private String status;

    private Date start;
    private Date end;

    public void copyFromCardUser(CardUser cardUser) {
        this.setUserName(cardUser.getUserName());
        this.setUserGroup(cardUser.getUserGroup());
        this.setCardUserId(cardUser.getUserId());
    }
}

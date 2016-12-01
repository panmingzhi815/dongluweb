package com.donglu.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by panmingzhi on 2016/12/1 0001.
 */
@Data
public class CardUser implements Serializable {

    private Long cardId;
    private Long userId;

    private String cardIdentifier;
    private String cardSerialNumber;
    private CardStatusEnum cardStatusEnum;

    private String userName;
    private String userIdentifier;
    private String userGroup;
    private String userIDCode;
}

package com.donglu.bean;

import lombok.Data;

import java.util.Date;

/**
 * Created by panmingzhi on 2017/5/11.
 */
@Data
public class Visitor {

    private Long id;
    private String name;
    private Date inTime;
    private Date outTime;
}

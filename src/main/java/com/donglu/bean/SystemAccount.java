package com.donglu.bean;

import lombok.Data;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Data
public class SystemAccount {
    private Long id;
    private String accountName;
    private String accountPassword;
    private String accountDescript;
}

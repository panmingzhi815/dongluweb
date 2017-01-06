package com.donglu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Druid 数据源
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Data
@Configuration
@ConfigurationProperties(prefix="spring.datasource", ignoreNestedProperties = false)
public class DruidDataSourceConfig {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int maxActive = 20;
    private int maxIdle = 20;
    private int minIdle = 1;
    private int initialSize = 1;
    private int maxWait = 60000;
    private int timeBetweenEvictionRunsMillis = 60000;
    private int minEictableIdleTimeMillis = 300000;
    private boolean testOnBorrow = false;
    private boolean testOnReturn = false;
    private boolean testWhileIdle = true;
    private String validationQuery;
    private boolean poolPreparedStatements = false;
    private int maxOpenPreparedStatements = 20;
    private String filters = "stat";

}
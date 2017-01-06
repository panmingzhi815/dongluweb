package com.donglu.config;

import com.donglu.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 使war包支持 tomcat 容器，打包时必须使用profile = tomcat
 * Created by panmingzhi on 2016/12/25 0025.
 */
public class WarSupport extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}

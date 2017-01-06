package com.donglu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Configuration
@EnableSwagger2
public class Swagger2Configure {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SPRING_WEB)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.donglu.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("东陆一卡通 RESTful APIs")
                .description("东陆一卡通web开放平台API调试平台")
                .contact("panmingzhi")
                .version("1.0")
                .build();
    }
}

package com.donglu.controller;

import com.donglu.bean.NoSecurity;
import com.google.common.io.Closer;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

/**
 * Created by panmingzhi on 2016/12/11 0011.
 */
@Controller
@RequestMapping("/imageController")
@Configuration
@ConfigurationProperties(prefix="spring.donglu.imagePath", ignoreNestedProperties = false)
@Data
public class ImageController {

    private static Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
    private String accessControlImagePath = "";

    @PostConstruct
    public void construct(){
        LOGGER.info("accessControlImagePath is: {}",accessControlImagePath);
    }

    @NoSecurity
    @RequestMapping(value = "/accessControlImage")
    public void getImage(@RequestParam String imageName, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        String[] split = imageName.split("_");
        String dateTime = split[0];
        String year = dateTime.substring(0,4);
        String month = dateTime.substring(4,6);
        String day = dateTime.substring(6,8);
        String hour = dateTime.substring(8,10);
        String min = dateTime.substring(10,12);
        String fullPath = new StringJoiner("\\").add(accessControlImagePath).add(year).add(month).add(day).add(hour).add(min).add(imageName).toString();

        Path source = Paths.get(fullPath);
        if (!Files.exists(source)) {
            try {
                URI uri = ClassLoader.getSystemClassLoader().getResource("image/not_found_user_image.png").toURI();
                source = Paths.get(uri);
            } catch (Exception e){
                LOGGER.error("获取本地默认图片失败",e);
            }
        }

        try (ServletOutputStream outputStream = response.getOutputStream()){
            Files.copy(source, outputStream);
            outputStream.flush();
            LOGGER.debug("输出图片成功 {}",fullPath);
        } catch (Exception e) {
            response.setStatus(404);
            LOGGER.error("输出图片失败",e);
        }
    }

}

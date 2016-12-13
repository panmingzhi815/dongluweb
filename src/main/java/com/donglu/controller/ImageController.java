package com.donglu.controller;

import com.donglu.bean.NoSecurity;
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
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * 获取图片
 * Created by panmingzhi on 2016/12/11 0011.
 */
@Controller
@RequestMapping("/imageController")
@Configuration
@ConfigurationProperties(prefix="spring.donglu.imagePath")
@Data
public class ImageController {

    public static final String IMAGE_NOT_FOUND_USER_IMAGE_PNG = "image/not_found_user_image.png";
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

        Optional<String> toFullPath = splitToFullPath(imageName, accessControlImagePath);
        Path source = null;

        if (toFullPath.isPresent()) {
            source = Paths.get(toFullPath.get());
        }

        if (source == null || !Files.exists(source)) {
            try {
                URL resource = ClassLoader.getSystemClassLoader().getResource(IMAGE_NOT_FOUND_USER_IMAGE_PNG);
                if (resource == null) {
                    throw new IOException("本地图片不存在：" + IMAGE_NOT_FOUND_USER_IMAGE_PNG);
                }
                URI uri = resource.toURI();
                source = Paths.get(uri);
            } catch (Exception e) {
                response.setStatus(400);
                LOGGER.error("获取本地默认图片失败", e);
                return;
            }
        }

        try (ServletOutputStream outputStream = response.getOutputStream()){
            Files.copy(source, outputStream);
            outputStream.flush();
            LOGGER.debug("输出图片成功 {}",source.toString());
        } catch (Exception e) {
            response.setStatus(404);
            LOGGER.error("输出图片失败",e);
        }
    }

    private Optional<String> splitToFullPath(String imageName, String accessControlImagePath) {
        String[] split = imageName.split("_");
        String dateTime = split[0];
        if (dateTime.length() != 12) {
            return Optional.empty();
        }
        String year = dateTime.substring(0,4);
        String month = dateTime.substring(4,6);
        String day = dateTime.substring(6,8);
        String hour = dateTime.substring(8,10);
        String min = dateTime.substring(10,12);
        String fullPath = new StringJoiner("\\").add(accessControlImagePath).add(year).add(month).add(day).add(hour).add(min).add(imageName).toString();
        return Optional.of(fullPath);
    }

}

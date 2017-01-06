package com.donglu.config;

import com.donglu.bean.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 * Created by panmingzhi on 2016/12/26 0026.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Response exceptionHandler(RuntimeException e, HttpServletResponse response) {
        LOGGER.error("服务器处理失败",e);
        return new Response().failureMsg("服务器处理失败");
    }
}

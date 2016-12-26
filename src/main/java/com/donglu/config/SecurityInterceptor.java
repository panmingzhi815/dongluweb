package com.donglu.config;

import com.donglu.bean.NoSecurity;
import com.donglu.bean.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Aspect
@Component
@Configuration
@ConfigurationProperties(prefix="spring.donglu.interceptor", ignoreNestedProperties = false)
public class SecurityInterceptor {

    public static final String KEY = "loginUser";
    public boolean enable = true;
    private Logger LOGGER = LoggerFactory.getLogger(SecurityInterceptor.class);

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Around("execution(* com.donglu.controller..*(..)) and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object interceptor(ProceedingJoinPoint point) throws Throwable {
        if (!enable) {
            LOGGER.debug("UserValidateInterceptor interceptor is disable");
            return point.proceed();
        }
        LOGGER.debug("UserValidateInterceptor interceptor start");

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(NoSecurity.class)){
            return point.proceed();
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session =request.getSession();
        if (session.getAttribute(KEY) == null){
            String message = "用户未登录";
            LOGGER.info(message);
            return new Response().failureMsg(message);
        }

        return point.proceed();
    }

    public void loginSession(Object object){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session =request.getSession();
        session.setAttribute(KEY,object);
    }

    public Object getLogin(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session =request.getSession();
        return session.getAttribute(KEY);
    }

    public void loginOut() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session =request.getSession();
        session.removeAttribute(KEY);
    }
}

package com.donglu.config;

import com.donglu.bean.NoSecurity;
import com.donglu.bean.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SecurityInterceptor {

    public static final String KEY = "loginUser";
    private Logger LOGGER = LoggerFactory.getLogger(SecurityInterceptor.class);

    @Around("execution(* com.donglu.controller..*(..)) and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object interceptor(ProceedingJoinPoint point) throws Throwable {
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
            return new Response().failure(message);
        }

        return point.proceed();
    }

    public void loginSesseion(Object object){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session =request.getSession();
        session.setAttribute(KEY,object);
    }

    public Object getLogin(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session =request.getSession();
        return session.getAttribute(KEY);
    }
}

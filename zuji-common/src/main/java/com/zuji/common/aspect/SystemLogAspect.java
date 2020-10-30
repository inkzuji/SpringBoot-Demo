package com.zuji.common.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 日志记录
 *
 * @author Ink足迹
 * @create 2020-02-04 11:07 上午
 **/
@Aspect
@Slf4j
@Component
public class SystemLogAspect {

    private HttpServletRequest request;

    @Resource
    ObjectMapper mapper;

    @Autowired(required = false)
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 方法规则拦截
     */
    @Pointcut("@annotation(com.zuji.common.aspect.annotation.AutoLog)")
    public void controllerPointerCut() {
    }


    @Before("controllerPointerCut()")
    public void before(JoinPoint joinPoint) throws JsonProcessingException {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                String paramName = paramNames[i];
                if (paramName == null || "token".equals(paramName) || "response".equals(paramName)) {
                    continue;
                }
                if (sb.length() > 0)
                    sb.append("&");
                if (args[i] instanceof Serializable)
                    sb.append(this.mapper.writeValueAsString(args[i]));
                else
                    sb.append(args[i]);
            }
            log.info("=================== {}.{} : URI = {}, 请求参数: {}", method.getDeclaringClass().getName(), method.getName(),
                    request.getRequestURI(), sb.toString());
        } else {
            log.info("=================== {}.{} : URI = {}, 请求参数: {}", method.getDeclaringClass().getName(), method.getName(),
                    request.getRequestURI(), "");
        }
    }

    @AfterReturning(value = "controllerPointerCut()", returning = "val")
    public void after(JoinPoint joinPoint, Object val) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("=================== {}.{} : URI = {}, 返回数据: {}", method.getDeclaringClass().getName(), method.getName(),
                request.getRequestURI(), new Gson().toJson(val));
    }

}

package com.zuji.filter;

import com.zuji.common.api.vo.Result;
import com.zuji.common.aspect.annotation.Common;
import com.zuji.common.constant.CommonConstant;
import com.zuji.common.system.util.JwtUtil;
import com.zuji.common.util.IPUtils;
import com.zuji.common.util.JacksonUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 自定义拦截器，判断此次请求是否有权限
 *
 * @author admin
 * @date 2018/5/11
 */
@Component
@Slf4j
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Value("${spring.profiles.active}")
    private String profilesActive;


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(CommonConstant.AUTHORIZATION);
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String servletPath = request.getServletPath();
        String IP = IPUtils.getIpAddr(request);

        log.info(">>>>>>>>>>>>>> 本次请求的token={} <<<<<<<<<<<", token);
        log.info(">>>>>>>>>>>>>> IP={}，接口路径={},\r\n请求参数={}", IP,
                servletPath,
                JacksonUtils.obj2Json(request.getParameterMap()));

        // 跳过认证
        if (method.isAnnotationPresent(Common.class)) {
            return true;
        }

        Result<Object> result = null;

        // 执行认证
        if (token == null) {
            return false;
        }

        // 解析 Token
        Claims tokenInfo = JwtUtil.verifyJWT(token);
        if (tokenInfo == null) {
            log.error(">>>>>>>>>>>>>> 访问拦截，token 非法,token解析错误, token={}", token);
            return false;
        }

        String activeEnv = tokenInfo.get("activeEnv", String.class);
        String ipAddress = tokenInfo.get("ipAddress", String.class);
        Long userId = tokenInfo.get("userId", Long.class);
        Integer sysUserId = tokenInfo.get("sysUserId", Integer.class);
        log.info(">>>>>>>>>>>>>> userId={},sysUserId={},activeEnv={},ipAddress={} <<<<<<<<<<<",
                userId, sysUserId, activeEnv, ipAddress);

        // 校验参数
        boolean isTokenValid = activeEnv == null || !StringUtils.equals(activeEnv, profilesActive)
                || ipAddress == null || !StringUtils.equals(ipAddress, IP)
                || userId == null || userId <= 0
                || sysUserId == null || sysUserId <= 0;

        if (isTokenValid) {
            log.error(">>>>>>>>>>>>>> 访问拦截，token 非法, userId={},sysUserId={},activeEnv={},ipAddress={},profilesActive={},IP={} <<<<<<<<<<<",
                    userId, sysUserId, activeEnv, ipAddress, profilesActive, IP);

            // result = ServerResponse.createByError(ResponseCode.RESULT_CODE_TOKEN_ERROR);
            // response.getWriter().write(JSON.toJSONString(result));
            return false;
        }

        return true;
    }
}
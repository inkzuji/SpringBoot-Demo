package com.zuji.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 过滤器
 *
 * @author admin
 */
@Configuration
@WebFilter(filterName = "securityFilter", urlPatterns = "/*")
@Slf4j
public class BasicSecurityFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("=============filter init===========");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>doFilter<<<<<<<<<<<<<<<<<<<<<");
        HttpServletRequest req = (HttpServletRequest) request;
        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(req);
        chain.doFilter(mutableRequest, response);
    }

    @Override
    public void destroy() {
        log.info("=============filter destroy===========");
    }
}  

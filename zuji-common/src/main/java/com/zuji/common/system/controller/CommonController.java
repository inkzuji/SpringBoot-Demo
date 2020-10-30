package com.zuji.common.system.controller;


import javax.servlet.http.HttpServletRequest;

import com.zuji.common.api.vo.Result;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/sys/common")
public class CommonController {


    @GetMapping("/403")
    public Result<?> noauth() {
        return Result.error("没有权限，请联系管理员授权");
    }


    /**
     * 把指定URL后的字符串全部截断当成参数
     * 这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
     */
    private static String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }

}

package com.zuji.common.system.controller;

import com.zuji.common.api.vo.Result;
import com.zuji.common.api.vo.ResultCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 错误控制器
 *
 * @author Ink足迹
 * @create 2020-08-01 17:15
 **/
@RestController
public class FilterErrorController extends BasicErrorController {
    public FilterErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    /**
     * 解决请求还未进入controller就跑出异常，无法被全局捕获异常捕获问题
     *
     * @param request request
     * @return 返回错误信息
     */
    @Override
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.values()));
        HttpStatus status = getStatus(request);

        // 构建 Result 对象
        String message = body.get("message").toString();
        Result<Object> result = Result.error(status.value(), message);
        String exception = (String) body.get("exception");
        if (StringUtils.isNotBlank(exception)
                && exception.equals(NullPointerException.class.getName())) {
            result.setCode(ResultCode.getErrCodeByMsg(message));
            status = HttpStatus.OK;
        }
        return new ResponseEntity(result, status);
    }
}

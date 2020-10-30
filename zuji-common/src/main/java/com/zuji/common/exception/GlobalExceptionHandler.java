package com.zuji.common.exception;

import com.zuji.common.api.vo.Result;
import com.zuji.common.api.vo.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Set;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DingTalkException.class)
    public Result<?> dingTalkException(DingTalkException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error(ResultCode.CODE_404);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return Result.error(ResultCode.CODE_501);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("不支持");
        sb.append(e.getMethod());
        sb.append("请求方法，");
        sb.append("支持以下");
        String[] methods = e.getSupportedMethods();
        if (methods != null) {
            for (String str : methods) {
                sb.append(str);
                sb.append("、");
            }
        }
        log.error(sb.toString(), e);
        return Result.error(ResultCode.CODE_405.getErrCode(), sb.toString());
    }

    /**
     * spring默认上传大小100MB 超出大小捕获异常MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage(), e);
        return Result.error(ResultCode.CODE_503);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        return Result.error(ResultCode.CODE_504);
    }

    @ExceptionHandler(PoolException.class)
    public Result<?> handlePoolException(PoolException e) {
        log.error(e.getMessage(), e);
        return Result.error(ResultCode.CODE_505);
    }

    /**
     * 参数校验失败
     *
     * @param e 异常信息
     * @return 返回信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> constraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        return Result.error(ResultCode.CODE_506);
    }

    /**
     * 参数校验失败
     *
     * @param e 异常信息
     * @return 返回信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        BindingResult br = e.getBindingResult();
        FieldError fe = br.getFieldError();
        String dm = Optional.ofNullable(fe).map(FieldError::getDefaultMessage).orElse("");
        return Result.error(dm);
    }

    /**
     * 参数绑定失败
     *
     * @param e 异常信息
     * @return 返回信息
     */
    @ExceptionHandler(BindException.class)
    public Result<?> bindException(BindException e) {
        log.error(e.getMessage(), e);
        BindingResult br = e.getBindingResult();
        FieldError fe = br.getFieldError();
        String dm = Optional.ofNullable(fe).map(FieldError::getDefaultMessage).orElse("");
        return Result.error(dm);
    }

    /**
     * 参数解析失败
     *
     * @param e 异常信息
     * @return 返回信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return Result.error(ResultCode.CODE_507);
    }

    /**
     * 缺少请求参数
     *
     * @param e 异常信息
     * @return 返回信息
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return Result.error("required_parameter_is_not_present:" + e.getParameterName());
    }

    /**
     * 监听空指针异常
     *
     * @param e 异常信息
     * @return 返回信息
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<?> nullPointerExpression(NullPointerException e) {
        log.error(e.getMessage(), e);
        return Result.error(ResultCode.CODE_406);
    }

    @ExceptionHandler(FatalBeanException.class)
    public Result<?> fatalBeanException(FatalBeanException e) {
        log.error(e.getMessage(), e);
        return Result.error(ResultCode.CODE_508);
    }


    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error();
    }

}

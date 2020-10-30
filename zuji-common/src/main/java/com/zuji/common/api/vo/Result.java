package com.zuji.common.api.vo;

import java.io.Serializable;

import com.zuji.common.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "接口返回对象", description = "接口返回对象")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    @ApiModelProperty(value = "成功标志")
    private boolean success = true;

    /**
     * 返回处理消息
     */
    @ApiModelProperty(value = "返回处理消息")
    private String message = "操作成功！";

    /**
     * 返回代码
     */
    @ApiModelProperty(value = "返回代码")
    private Integer code = 0;

    /**
     * 返回数据对象 data
     */
    @ApiModelProperty(value = "返回数据对象")
    private T result;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private long timestamp = System.currentTimeMillis();

    public Result() {
    }

    public static <T> Result<T> success() {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS.getErrCode());
        r.setMessage(ResultCode.SUCCESS.getErrMsg());
        return r;
    }

    public static <T> Result<T> success(String msg) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS.getErrCode());
        r.setMessage(msg);
        return r;
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS.getErrCode());
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> error() {
        return error(ResultCode.FAIL);
    }

    public static <T> Result<T> error(String msg) {
        return error(ResultCode.FAIL.getErrCode(), msg);
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        return error(resultCode.getErrCode(), resultCode.getErrMsg());
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> r = new Result<T>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
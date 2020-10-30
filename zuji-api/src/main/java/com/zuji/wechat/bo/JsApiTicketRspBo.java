package com.zuji.wechat.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 授权页ticket
 * @author Ink足迹
 * @create 2020-06-24 17:47
 **/
@Getter
@Setter
public class JsApiTicketRspBo implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 临时票据，用于在获取授权链接时作为参数传入
     */
    @JsonProperty("ticket")
    private String ticket;

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;


    @JsonProperty("errmsg")
    private String errMsg;

    @JsonProperty("errcode")
    private Integer errCode;

    public boolean isSuccess() {
        return null == this.getErrCode() || Objects.equals(0, this.getErrCode());
    }

    public Integer getErrCode() {
        return errCode;
    }
}

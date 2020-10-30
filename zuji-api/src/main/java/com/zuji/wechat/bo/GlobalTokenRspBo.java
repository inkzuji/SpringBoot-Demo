package com.zuji.wechat.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 全局token返回参数
 *
 * @author Ink足迹
 * @create 2020-06-24 16:59
 **/
@Getter
@Setter
public class GlobalTokenRspBo implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 获取到的凭证
     */
    @JsonProperty("access_token")
    private String accessToken;

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

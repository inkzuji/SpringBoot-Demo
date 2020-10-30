package com.zuji.wechat.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 扫码登录返回参数
 *
 * @author Ink足迹
 * @create 2020-06-15 16:26
 **/
@Getter
@Setter
public class AuthorizeLoginByCodeRspBo implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 接口调用凭证
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;

    /**
     * 用户刷新access_token
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * 授权用户唯一标识
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;

    /**
     * 当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
     */
    @JsonProperty("unionid")
    private String unionId;

    @JsonProperty("errmsg")
    private String errMsg;

    @JsonProperty("errcode")
    private Integer errCode;

    public boolean isSuccess() {
        return null == this.getErrCode() || Objects.equals(0L, this.getErrCode());
    }

    public Integer getErrCode() {
        return errCode;
    }
}

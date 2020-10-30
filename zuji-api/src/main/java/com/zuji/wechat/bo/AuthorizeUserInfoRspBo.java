package com.zuji.wechat.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 微信网站网页用户个人信息返回参数
 *
 * @author Ink足迹
 * @create 2020-06-15 17:22
 **/
@Getter
@Setter
public class AuthorizeUserInfoRspBo implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 普通用户的标识，对当前开发者帐号唯一
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * 普通用户昵称
     */
    @JsonProperty("nickname")
    private String nickName;

    /**
     * 普通用户性别，1为男性，2为女性
     */
    @JsonProperty("sex")
    private Integer sex;

    /**
     * 普通用户个人资料填写的省份
     */
    @JsonProperty("province")
    private String province;

    /**
     * 普通用户个人资料填写的城市
     */
    @JsonProperty("city")
    private String city;

    /**
     * 国家，如中国为CN
     */
    @JsonProperty("country")
    private String country;

    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
     */
    @JsonProperty("headimgurl")
    private String headImgUrl;

    /**
     * 用户特权信息，json数组，
     */
    @JsonProperty("privilege")
    private List<String> privilege;

    /**
     * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     */
    @JsonProperty("unionid")
    private String unionId;

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

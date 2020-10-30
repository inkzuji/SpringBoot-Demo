package com.zuji.wechat.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 单次分账请求参数
 *
 * @author Ink足迹
 * @create 2020-06-30 18:58
 **/
@Getter
@Setter
public class WeChatProfitSharingReqBo implements Serializable {
    private static final long serialVersionUID = 2L;

    public WeChatProfitSharingReqBo() {
    }

    /**
     * 公众账号ID
     */
    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 微信退支付key
     */
    private String payKey;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 商户分账单号
     */
    private String outOrderNo;

    /**
     * 分账接收方帐号
     * <p>
     * 类型是MERCHANT_ID时，是商户ID
     * <p>
     * 类型是PERSONAL_OPENID时，是个人openid
     */
    private String account;

    /**
     * 分账金额
     */
    private int amount;

    /**
     * 分账描述
     */
    private String description;

}

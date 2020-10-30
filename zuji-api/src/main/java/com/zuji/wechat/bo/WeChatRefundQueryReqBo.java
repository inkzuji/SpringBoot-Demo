package com.zuji.wechat.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 微信支付｜申请退款所需参数
 *
 * @author Ink足迹
 * @create 2020-06-30 14:41
 **/
@Getter
@Setter
public class WeChatRefundQueryReqBo implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 公众账号ID
     */
    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 微信退款单号
     */
    private String payKey;

    /**
     * 微信退款单号
     */
    private String refundId;
}

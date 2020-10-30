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
public class WeChatRefundReqBo implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 小程序ID
     */
    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户key
     */
    private String payKey;

    /**
     * 微信订单号
     */
    private String transactionId;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 订单金额
     */
    private String totalFee;

    /**
     * 退款金额
     */
    private String refundFee;

    /**
     * 判断是否需要余额支付
     */
    private boolean needBalance = false;
}

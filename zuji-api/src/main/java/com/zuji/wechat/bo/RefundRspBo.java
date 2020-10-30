package com.zuji.wechat.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 退款返回参数
 *
 * @author Ink足迹
 * @create 2020-06-30 16:36
 **/
@Getter
@Setter
public class RefundRspBo implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 商户退款订单号
     */
    private String outRefundNo;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 微信订单号
     */
    private String transactionId;

    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 总金额
     */
    private int totalFee;

    /**
     * 退款金额
     */
    private int refundFee;

    /**
     * xml 原始字符串
     */
    private String xmlString;


    /**
     * 判断是否需要余额支付
     */
    private boolean needBalance = false;

    private String errorCode;

    private String errorMsg;
}

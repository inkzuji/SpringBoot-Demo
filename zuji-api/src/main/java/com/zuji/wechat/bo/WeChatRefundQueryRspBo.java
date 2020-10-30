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
public class WeChatRefundQueryRspBo implements Serializable {
    private static final long serialVersionUID = 2L;
    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 微信订单号
     */
    private String transactionId;

    /**
     * 商户退款订单号
     */
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 订单金额
     */
    private int totalFee;

    /**
     * 申请退款金额
     */
    private int refundFee;

    /**
     * 退款笔数
     */
    private int refundCount;

    /**
     * 退款状态 SUCCESS—退款成功 REFUNDCLOSE—退款关闭。 PROCESSING—退款处理中 CHANGE—退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。
     * $n为下标，从0开始编号。
     */
    private String refundStatus;

    /**
     * 退款入账账户
     */
    private String refundRecvAccount;

    /**
     * 退款成功时间，当退款状态为退款成功时有返回
     * <p>
     * 2016-07-25 15:26:26
     */
    private String refundSuccessTime;
}

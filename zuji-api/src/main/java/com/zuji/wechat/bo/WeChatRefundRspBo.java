package com.zuji.wechat.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 微信支付｜统一下单所需参数
 *
 * @author Ink足迹
 * @create 2020-06-30 14:41
 **/
@Getter
@Setter
public class WeChatRefundRspBo implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 返回状态码
     * <p>
     * SUCCESS/FAIL
     * <p>
     * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
     */
    @JsonProperty("return_code")
    private String returnCode;

    /**
     * 返回信息
     * <p>
     * 当return_code为FAIL时返回信息为错误原因 ，
     * <p>
     * 例如: 签名失败, 参数格式校验错误
     */
    @JsonProperty("return_msg")
    private String returnMsg;

    // ==============以下字段在return_code为SUCCESS的时候有返回========

    /**
     * 业务结果
     * <p>
     * SUCCESS/FAIL
     */
    @JsonProperty("result_code")
    private String resultCode;

    /**
     * 错误代码
     * <p>
     * 当result_code为FAIL时返回错误代码
     */
    @JsonProperty("err_code")
    private String errCode;

    /**
     * 错误代码描述
     * <p>
     * 当result_code为FAIL时返回错误描述
     */
    @JsonProperty("err_code_des")
    private String errCodeDes;

    /**
     * 公众账号ID
     * <p>
     * 调用接口提交的公众账号ID
     */

    @JsonProperty("appid")
    private String appId;

    /**
     * 商户号
     * <p>
     * 调用接口提交的商户号
     */
    @JsonProperty("mch_id")
    private String mchId;

    /**
     * 随机字符串
     * <p>
     * 微信返回的随机字符串
     */
    @JsonProperty("nonce_str")
    private String nonceStr;

    /**
     * 签名
     * <p>
     * 微信返回的签名值
     */
    @JsonProperty("sign")
    private String sign;

    /**
     * 微信订单号
     */
    @JsonProperty("transaction_id")
    private String transactionId;

    /**
     * 商户订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /**
     * 商户退款单号
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    @JsonProperty("refund_id")
    private String refundId;

    /**
     * 退款金额,单位为分
     */
    @JsonProperty("refund_fee")
    private Integer refundFee;

    /**
     * 标价金额
     */
    @JsonProperty("total_fee")
    private Integer totalFee;

    /**
     * 现金支付金额,单位为分
     */
    @JsonProperty("cash_fee")
    private Integer cashFee;

    /**
     * 通信是否成功
     *
     * @return true｜false
     */
    public boolean isReturnSuccess() {
        return "SUCCESS".equals(this.returnCode);
    }

    /**
     * 业务是否成功
     *
     * @return true｜false
     */
    public boolean isResultSuccess() {
        return "SUCCESS".equals(this.resultCode);
    }
}

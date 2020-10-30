package com.zuji.wechat.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class WeChatProfitSharingRspBo implements Serializable {
    private static final long serialVersionUID = 2L;

    public WeChatProfitSharingRspBo() {
    }

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
     * 微信返回的签名
     */
    @JsonProperty("sign")
    private String sign;

    /**
     * 微信订单号
     * <p>
     * 微信支付订单号
     */
    @JsonProperty("transaction_id")
    private String transactionId;

    /**
     * 商户分账单号
     * <p>
     * 调用接口提供的商户系统内部的分账单号
     */
    @JsonProperty("out_order_no")
    private String outOrderNo;

    /**
     * 微信分账单号
     * <p>
     * 微信分账单号，微信系统返回的唯一标识
     */
    @JsonProperty("order_id")
    private String orderId;

    public boolean isSuccess() {
        return "SUCCESS".equals(this.returnCode) && "SUCCESS".equals(this.resultCode);
    }
}

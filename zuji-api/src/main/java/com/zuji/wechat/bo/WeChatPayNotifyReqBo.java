package com.zuji.wechat.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信支付结果通知返回参数
 *
 * @author Ink足迹
 * @create 2020-07-02 10:51
 **/
@Getter
@Setter
public class WeChatPayNotifyReqBo implements Serializable {

    private static final long serialVersionUID = 2L;

    public boolean isSuccess() {
        return "SUCCESS".equals(this.returnCode) && "SUCCESS".equals(this.resultCode);
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
     * 设备号
     * <p>
     * 自定义参数，可以为请求支付的终端设备号等
     */
    @JsonProperty("device_info")
    private String deviceInfo;

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
     * 签名类型
     * <p>
     * 目前支持HMAC-SHA256和MD5，默认为MD5
     */
    @JsonProperty("sign_type")
    private String signType;

    /**
     * 用户标识
     * <p>
     * 用户在商户appid下的唯一标识
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * 是否关注公众账号
     * <p>
     * 用户是否关注公众账号，Y-关注，N-未关注
     */
    @JsonProperty("is_subscribe")
    private String isSubscribe;

    /**
     * 交易类型
     * <p>
     * JSAPI、NATIVE、APP
     */
    @JsonProperty("trade_type")
    private String tradeType;

    /**
     * 付款银行
     * <p>
     * 银行类型，采用字符串类型的银行标识，银行类型见银行列表
     */
    @JsonProperty("bank_type")
    private String bankType;

    /**
     * 订单金额
     * <p>
     * 订单总金额，单位为分
     */
    @JsonProperty("total_fee")
    private Integer totalFee;

    /**
     * 应结订单金额
     * <p>
     * 应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    @JsonProperty("settlement_total_fee")
    private Integer settlementTotalFee;

    /**
     * 货币种类
     * <p>
     * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @JsonProperty("fee_type")
    private String feeType;

    /**
     * 现金支付金额
     * <p>
     * 现金支付金额订单现金支付金额，详见支付金额
     */
    @JsonProperty("cash_fee")
    private Integer cashFee;

    /**
     * 现金支付货币类型
     * <p>
     * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @JsonProperty("cash_fee_type")
    private String cashFeeType;

    /**
     * 总代金券金额
     * <p>
     * 代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
     */
    @JsonProperty("coupon_fee")
    private Integer couponFee;

    /**
     * 代金券使用数量
     * <p>
     * 代金券使用数量
     */
    @JsonProperty("coupon_count")
    private Integer couponCount;

    /**
     * 微信支付订单号
     * <p>
     * 微信支付订单号
     */
    @JsonProperty("transaction_id")
    private String transactionId;

    /**
     * 商户订单号
     * <p>
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /**
     * 商家数据包
     * <p>
     * 商家数据包，原样返回
     */
    @JsonProperty("attach")
    private String attach;

    /**
     * 支付完成时间
     * <p>
     * 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyyMMddHHmmss")
    @JsonProperty("time_end")
    private Date timeEnd;
}

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
public class WeChatUnifiedOrderRspBo implements Serializable {
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

    // ================以下字段在return_code 和result_code都为SUCCESS的时候有返回========

    /**
     * 交易类型
     */
    @JsonProperty("trade_type")
    private String tradeType;

    /**
     * 预支付交易会话标识
     * <p>
     * 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    @JsonProperty("prepay_id")
    private String prepayId;

    /**
     * 二维码链接
     * <p>
     * trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
     */
    @JsonProperty("code_url")
    private String codeUrl;

    public boolean isSuccess() {
        return "SUCCESS".equals(this.returnCode) && "SUCCESS".equals(this.resultCode);
    }
}

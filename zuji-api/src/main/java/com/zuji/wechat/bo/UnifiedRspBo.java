package com.zuji.wechat.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 新增订单返回参数
 *
 * @author Ink足迹
 * @create 2020-06-30 15:58
 **/
@Getter
@Setter
public class UnifiedRspBo implements Serializable {

    private static final long serialVersionUID = 2L;

    @ApiModelProperty("是否需要支付：true->是；false->否；表示订单金额是否为0")
    private boolean needPay;

    @ApiModelProperty("订单ID")
    private Integer orderId;

    @ApiModelProperty("需要支付的情况下 ，true->调用微信支付成功；false->调用微信支付失败；")
    private boolean success;

    @ApiModelProperty("随机字符串，长度为32个字符以下")
    private String nonceStr;

    @ApiModelProperty("即package：统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=***")
    private String prepayId;

    @ApiModelProperty("签名算法")
    private String signType = "MD5";

    @ApiModelProperty("签名")
    private String paySign;

    @ApiModelProperty("时间戳")
    private String timeStamp;

    @ApiModelProperty("appId")
    private String appId;

    @ApiModelProperty("支付跳转链接|H5支付才会传回")
    private String webUrl;
}

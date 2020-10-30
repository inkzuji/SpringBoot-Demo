package com.zuji.wechat.bo;

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
public class WeChatUnifiedOrderReqBo implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 微信支付的商户密钥
     */
    private String payKey;

    /**
     * 商户号:String(32)
     */
    private String mchId;

    /**
     * 商品描述:String(128)
     */
    private String body;

    /**
     * 附加数据:String(127)
     * 用于存储公司名称
     */
    private String attach;

    /**
     * 商户订单号:String(32)
     */
    private String outTradeNo;

    /**
     * 订单总金额，单位为分
     */
    private int totalFee;

    /**
     * 终端IP:String(64)
     */
    private String spbillCreateIp;

    /**
     * 用户标识:String(128)
     */
    private String openId;

    /**
     * 是否分账，默认否
     */
    private boolean profitSharing = false;

    public boolean isProfitSharing() {
        return profitSharing;
    }

    public void setProfitSharing(boolean profitSharing) {
        this.profitSharing = profitSharing;
    }
}

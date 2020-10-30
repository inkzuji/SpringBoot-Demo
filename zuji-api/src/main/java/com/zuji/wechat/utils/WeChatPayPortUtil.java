package com.zuji.wechat.utils;

import com.zuji.config.WeChatConfig;
import com.zuji.wechat.bo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.zuji.common.util.JacksonUtils;
import com.zuji.common.util.LocalDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 微信普通商户支付工具类
 *
 * @author Ink足迹
 * @create 2020-06-30 14:36
 **/
@Slf4j
public class WeChatPayPortUtil {

    /**
     * 微信支付｜统一下单
     *
     * @param reqBo 下单请求参数
     * @return 返回前端唤起支付的参数
     */
    public static UnifiedRspBo unifiedOrder(WeChatUnifiedOrderReqBo reqBo) {
        log.info(">>>>>> 微信支付｜统一下单. 请求参数={}", JacksonUtils.obj2Json(reqBo));

        // 生成的随机字符串
        String nonceStr = WXPayUtil.generateNonceStr();

        // 组装参数，用户生成统一下单接口的签名

        // 订单失效时间
        LocalDateTime localTime = LocalDateTime.now();

        // 订单10分钟过期
        localTime = localTime.plusMinutes(10);
        String timeExpire = LocalDateUtils.formatLocalDateTimeToString(localTime, LocalDateUtils.TIME_PATTERN_SECOND);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", reqBo.getAppId());
        paramMap.put("mch_id", reqBo.getMchId());
        paramMap.put("nonce_str", nonceStr);
        paramMap.put("sign_type", WeChatConfig.PAY_SIGN_TYPE);

        String body = reqBo.getBody();
        paramMap.put("body", body.length() > 32 ? body.substring(0, 32) : body);

        if (StringUtils.isNotBlank(reqBo.getAttach()))
            paramMap.put("attach", reqBo.getAttach());

        paramMap.put("out_trade_no", reqBo.getOutTradeNo());

        // 支付金额，这边需要转成字符串类型，否则后面的签名会失败,单位为分
        paramMap.put("total_fee", String.valueOf(reqBo.getTotalFee()));
        paramMap.put("spbill_create_ip", reqBo.getSpbillCreateIp());
        paramMap.put("time_expire", timeExpire);

        // 支付成功后的回调地址
        paramMap.put("notify_url", WeChatConfig.getReceiveAccountPayNotifyUrl());
        paramMap.put("trade_type", WeChatConfig.PAY_TRADE_TYPE);
        paramMap.put("openid", reqBo.getOpenId());
        if (reqBo.isProfitSharing())
            paramMap.put("profit_sharing", "Y");

        UnifiedRspBo rspVo = new UnifiedRspBo();
        rspVo.setNeedPay(true);
        rspVo.setSuccess(false);
        try {
            // MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mySign = WXPayUtil.generateSignature(paramMap, reqBo.getPayKey());

            // 拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            paramMap.put("sign", mySign);
            log.info(">>>>>> 微信支付｜统一下单. 请求微信支付数据：" + JacksonUtils.obj2Json(paramMap));

            WxPayMyConfig wxPayMyConfig = new WxPayMyConfig();
            wxPayMyConfig.setAppId(reqBo.getAppId());
            wxPayMyConfig.setKey(reqBo.getPayKey());
            wxPayMyConfig.setMchID(reqBo.getMchId());

            WXPay wxPay = new WXPay(wxPayMyConfig);

            // 调用统一下单接口，并接受返回的结果
            Map<String, String> result = wxPay.unifiedOrder(paramMap);
            log.info(">>>>>> 微信支付｜统一下单. 调用微信支付返回数据：" + JacksonUtils.obj2Json(result));

            WeChatUnifiedOrderRspBo rspBo = JacksonUtils.obj2Obj(result, new TypeReference<WeChatUnifiedOrderRspBo>() {
            });

            // 成功
            if (rspBo.isSuccess()) {
                rspVo.setSuccess(true);
                rspVo.setAppId(reqBo.getAppId());
                rspVo.setNonceStr(nonceStr);

                // 返回的预付单信息
                String prepayId = MessageFormat.format("prepay_id={0}", rspBo.getPrepayId());
                rspVo.setPrepayId(prepayId);

                // 这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                long timeStamp = System.currentTimeMillis() / 1000;
                rspVo.setTimeStamp(Long.toString(timeStamp));

                // 拼接签名需要的参数
                HashMap<String, String> signParamMap = new HashMap<>();
                signParamMap.put("appId", reqBo.getAppId());
                signParamMap.put("timeStamp", Long.toString(timeStamp));
                signParamMap.put("nonceStr", nonceStr);
                signParamMap.put("package", prepayId);
                signParamMap.put("signType", WeChatConfig.PAY_SIGN_TYPE);
                log.info(">>>>>> 微信支付｜统一下单成功. 需要二次签名参数：" + JacksonUtils.obj2Json(signParamMap));
                String secondSign = WXPayUtil.generateSignature(signParamMap, reqBo.getPayKey());
                log.info("微信支付|统一下单|小程序支付/JSAPI支付|统一下单成功，返回的签名参数：" + secondSign);
                rspVo.setPaySign(secondSign);
            }
        } catch (Exception e) {
            log.info(">>>>>> 微信支付｜统一下单失败. ", e);
        }
        return rspVo;
    }

    /**
     * 微信支付｜申请退款
     *
     * @param reqBo 请求参数
     * @return 退款返回结果
     */
    public static RefundRspBo refund(WeChatRefundReqBo reqBo) {
        log.info(">>>>>> 微信支付｜申请退款. 请求参数：{}", JacksonUtils.obj2Json(reqBo));
        // 生成的随机字符串
        String nonceStr = WXPayUtil.generateNonceStr();

        // 组装参数，用户生成统一下单接口的签名
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("appid", reqBo.getAppId());
        paramMap.put("mch_id", reqBo.getMchId());
        paramMap.put("nonce_str", nonceStr);
        paramMap.put("sign_type", WeChatConfig.PAY_SIGN_TYPE);
        paramMap.put("transaction_id", reqBo.getTransactionId());
        paramMap.put("out_refund_no", reqBo.getOutRefundNo());
        paramMap.put("total_fee", reqBo.getTotalFee());
        paramMap.put("refund_fee", reqBo.getRefundFee());

        // 调用可用余额退款
        if (reqBo.isNeedBalance())
            paramMap.put("refund_account", "REFUND_SOURCE_RECHARGE_FUNDS");
        paramMap.put("notify_url", "");
        log.info(">>>>>> 微信支付｜申请退款. 调用签名参数：{}", JacksonUtils.obj2Json(paramMap));

        // 返回对象
        RefundRspBo rspVo = new RefundRspBo();
        rspVo.setSuccess(false);
        try {
            String mySign = WXPayUtil.generateSignature(paramMap, reqBo.getPayKey());
            paramMap.put("sign", mySign);
            log.info(">>>>>> 微信支付｜申请退款. 申请微信退款参数: {}", JacksonUtils.obj2Json(paramMap));

            WxPayMyConfig wxMyConfig = new WxPayMyConfig();
            wxMyConfig.setAppId(reqBo.getAppId());
            wxMyConfig.setKey(reqBo.getPayKey());
            wxMyConfig.setMchID(reqBo.getMchId());

            WXPay wxPay = new WXPay(wxMyConfig);

            // 调用申请退款接口，并接受返回的结果
            Map<String, String> result = wxPay.refund(paramMap);
            log.info(">>>>>> 微信支付｜申请退款. 申请退款返回参数：{}", JacksonUtils.obj2Json(result));

            WeChatRefundRspBo refundRspBo = JacksonUtils.obj2Obj(result, new TypeReference<WeChatRefundRspBo>() {
            });
            if (refundRspBo.isReturnSuccess()) {
                if (refundRspBo.isResultSuccess()) {
                    rspVo.setSuccess(true);
                    rspVo.setOutTradeNo(refundRspBo.getOutTradeNo());
                    rspVo.setOutRefundNo(refundRspBo.getOutRefundNo());
                    rspVo.setTransactionId(refundRspBo.getTransactionId());
                    rspVo.setRefundId(refundRspBo.getRefundId());
                    rspVo.setTotalFee(refundRspBo.getTotalFee());
                    rspVo.setRefundFee(refundRspBo.getRefundFee());
                } else {
                    rspVo.setSuccess(false);
                    rspVo.setErrorCode(refundRspBo.getErrCode());
                    rspVo.setErrorMsg(refundRspBo.getErrCodeDes());

                    if (StringUtils.equals("NOTENOUGH", refundRspBo.getErrCode())
                            && StringUtils.equals("交易未结算资金不足，请使用可用余额退款", refundRspBo.getErrCodeDes())) {
                        rspVo.setNeedBalance(true);
                    }
                }
            } else {
                rspVo.setSuccess(false);
                rspVo.setErrorCode(refundRspBo.getErrCode());
                rspVo.setErrorMsg(refundRspBo.getErrCodeDes());
            }
        } catch (Exception e) {
            log.error(">>>>>> 微信支付｜申请退款失败. ", e);
        }
        return rspVo;
    }

    /**
     * 微信支付｜查询退款
     *
     * @param reqBo 请求参数
     * @return 返回结果
     */
    public static WeChatRefundQueryRspBo refundQuery(WeChatRefundQueryReqBo reqBo) {
        log.info(">>>>>> 微信支付｜查询退款. 请求参数：{}", JacksonUtils.obj2Json(reqBo));
        //生成的随机字符串
        String nonceStr = WXPayUtil.generateNonceStr();

        // 组装参数，用户生成统一下单接口的签名
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("appid", reqBo.getAppId());
        paramMap.put("mch_id", reqBo.getMchId());
        paramMap.put("nonce_str", nonceStr);
        paramMap.put("sign_type", WeChatConfig.PAY_SIGN_TYPE);
        paramMap.put("refund_id", reqBo.getRefundId());
        log.info(">>>>>> 微信支付｜查询退款. 调用签名参数：{}", JacksonUtils.obj2Json(paramMap));

        // 返回对象
        WeChatRefundQueryRspBo rspBo = new WeChatRefundQueryRspBo();
        rspBo.setSuccess(false);
        try {
            String mySign = WXPayUtil.generateSignature(paramMap, reqBo.getPayKey());
            paramMap.put("sign", mySign);
            log.info(">>>>>> 微信支付｜申请退款. 申请微信退款参数: {}", JacksonUtils.obj2Json(paramMap));

            WxPayMyConfig wxMyConfig = new WxPayMyConfig();
            wxMyConfig.setAppId(reqBo.getAppId());
            wxMyConfig.setKey(reqBo.getPayKey());
            wxMyConfig.setMchID(reqBo.getMchId());

            WXPay wxPay = new WXPay(wxMyConfig);

            // 调用退款查询接口，并接受返回的结果
            Map<String, String> result = wxPay.refundQuery(paramMap);
            log.info(">>>>>> 微信支付｜申请退款. 申请退款返回参数：{}", JacksonUtils.obj2Json(result));

            // 返回状态码
            String returnCode = result.get("return_code");
            String resultCode = result.get("result_code");
            if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)) {
                rspBo.setSuccess(true);
                rspBo.setOutTradeNo(result.get("out_trade_no"));
                rspBo.setTransactionId(result.get("transaction_id"));
                rspBo.setTotalFee(Integer.parseInt(result.get("total_fee")));
                rspBo.setOutRefundNo(result.get("out_refund_no_0"));
                rspBo.setRefundId(result.get("refund_id_0"));
                rspBo.setRefundFee(Integer.parseInt(result.get("refund_fee_0")));
                rspBo.setRefundCount(Integer.parseInt(result.get("refund_count")));
                rspBo.setRefundRecvAccount(result.get("refund_recv_accout_0"));
                rspBo.setRefundStatus(result.get("refund_status_0"));
                rspBo.setRefundSuccessTime(result.get("refund_success_time_0"));
            }
        } catch (Exception e) {
            log.error(">>>>>> 微信支付｜申请退款失败.", e);
        }
        return rspBo;
    }

    /**
     * 普通直连分账接口｜请求单次分账
     *
     * @param reqBo 请求参数
     * @return 返回结果
     */
    public static WeChatProfitSharingRspBo profitSharing(WeChatProfitSharingReqBo reqBo) {
        log.info(">>>>>> 普通直连分账接口｜请求单次分账. 请求参数：{}", JacksonUtils.obj2Json(reqBo));

        //生成的随机字符串
        String nonceStr = WXPayUtil.generateNonceStr();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("mch_id", reqBo.getMchId());
        paramMap.put("appid", reqBo.getAppId());
        paramMap.put("nonce_str", nonceStr);
        paramMap.put("sign_type", WeChatConfig.PROFIT_SHARING_SIGN_TYPE);
        paramMap.put("transaction_id", reqBo.getTransactionId());
        paramMap.put("out_order_no", reqBo.getOutOrderNo());
        List<HashMap<String, Object>> childList = new ArrayList<>();
        HashMap<String, Object> childMap = new HashMap<>();
        childMap.put("type", WeChatConfig.PROFIT_SHARING_PERSONAL_OPENID);
        childMap.put("account", reqBo.getAccount());
        childMap.put("amount", reqBo.getAmount());
        childMap.put("description", reqBo.getDescription());
        childList.add(childMap);
        paramMap.put("receivers", JacksonUtils.obj2Json(childList));
        log.info(">>>>>> 普通直连分账接口｜请求单次分账. 调用签名参数：{}", JacksonUtils.obj2Json(paramMap));

        WeChatProfitSharingRspBo rspBo = null;
        try {
            String sign = WXPayUtil.generateSignature(paramMap, reqBo.getPayKey(), WXPayConstants.SignType.HMACSHA256);
            paramMap.put("sign", sign);
            log.info(">>>>>> 普通直连分账接口｜请求单次分账. 签名结束之后：{}", JacksonUtils.obj2Json(paramMap));

            WxPayMyConfig wxMyConfig = new WxPayMyConfig();
            wxMyConfig.setAppId(reqBo.getAppId());
            wxMyConfig.setKey(reqBo.getPayKey());
            wxMyConfig.setMchID(reqBo.getMchId());

            WXPay wxPay = new WXPay(wxMyConfig);

            // 调用请求单次分账接口，并接受返回的结果
            String result = wxPay.requestWithCert(WeChatConfig.PROFIT_SHARING_URL, paramMap, WeChatConfig.TIME_OUT_MS, WeChatConfig.TIME_OUT_MS);
            log.info(">>>>>> 普通直连分账接口｜请求单次分账. 调用单次分账接口返回结果：{}", result);

            Map<String, String> res = WXPayUtil.xmlToMap(result);
            rspBo = JacksonUtils.obj2Obj(res, new TypeReference<WeChatProfitSharingRspBo>() {
            });
        } catch (Exception e) {
            log.error(">>>>>> 普通直连分账接口｜请求单次分账失败. ", e);
        }
        return Optional.ofNullable(rspBo).orElseGet(WeChatProfitSharingRspBo::new);
    }

    /**
     * 添加分账接收方
     *
     * @param payKey  支付key
     * @param mchId   商户号
     * @param appId   公众账号ID
     * @param type    分账接收方类型
     * @param account 分账接收方帐号
     * @return 返回是否添加陈宫
     * @title profitSharingAddReceiver
     * @modifyDate 2020-08-11
     * @modifyUser Ink足迹
     * @createDate 2020-08-11
     * @createUser Ink足迹
     */
    public static boolean profitSharingAddReceiver(String payKey, String mchId, String appId, String type, String account) {

        log.info(">>>>>>> 普通直连分账接口|添加分账接收方。start params: payKey={}, mchId={}, appId={}, type={}, account={}",
                payKey, mchId, appId, type, account);

        //生成的随机字符串
        String nonceStr = WXPayUtil.generateNonceStr();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("mch_id", mchId);
        paramMap.put("appid", appId);
        paramMap.put("nonce_str", nonceStr);
        paramMap.put("sign_type", WeChatConfig.PROFIT_SHARING_SIGN_TYPE);

        HashMap<String, String> receiverMap = new HashMap<>();
        receiverMap.put("type", type);
        receiverMap.put("account", account);
        receiverMap.put("relation_type", "USER");
        paramMap.put("receiver", JacksonUtils.obj2Json(receiverMap));
        try {
            String sign = WXPayUtil.generateSignature(paramMap, payKey, WXPayConstants.SignType.HMACSHA256);
            paramMap.put("sign", sign);
            log.info(">>>>>> 普通直连分账接口|添加分账接收方. 签名结束之后：{}", JacksonUtils.obj2Json(paramMap));

            WxPayMyConfig wxMyConfig = new WxPayMyConfig();
            wxMyConfig.setAppId(appId);
            wxMyConfig.setKey(payKey);
            wxMyConfig.setMchID(mchId);

            WXPay wxPay = new WXPay(wxMyConfig);

            // 调用请求单次分账接口，并接受返回的结果
            String result = wxPay.requestWithoutCert(WeChatConfig.PROFIT_SHARING_ADD_RECEIVER_URL, paramMap,
                    WeChatConfig.TIME_OUT_MS, WeChatConfig.TIME_OUT_MS);
            log.info(">>>>>> 普通直连分账接口｜添加分账接收方. 返回结果：{}", result);

            Map<String, String> res = WXPayUtil.xmlToMap(result);
            String returnCode = res.get("return_code");
            String resultCode = res.get("result_code");
            if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)) {
                return true;
            }

        } catch (Exception e) {
            log.error(">>>>>> 普通直连分账接口｜添加分账接收方. ", e);
        }
        return false;
    }

    /**
     * 删除分账接收方
     *
     * @param payKey  支付key
     * @param mchId   商户号
     * @param appId   公众账号ID
     * @param account 分账接收方帐号
     * @return 返回是否添加陈宫
     * @title profitSharingAddReceiver
     * @modifyDate 2020-08-11
     * @modifyUser Ink足迹
     * @createDate 2020-08-11
     * @createUser Ink足迹
     */
    public static boolean profitSharingRemoveReceiver(String payKey, String mchId, String appId, String account) {

        // 生成的随机字符串
        String nonceStr = WXPayUtil.generateNonceStr();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("mch_id", mchId);
        paramMap.put("appid", appId);
        paramMap.put("nonce_str", nonceStr);
        paramMap.put("sign_type", WeChatConfig.PROFIT_SHARING_SIGN_TYPE);

        HashMap<String, String> receiverMap = new HashMap<>();
        receiverMap.put("type", WeChatConfig.PROFIT_SHARING_PERSONAL_OPENID);
        receiverMap.put("account", account);
        paramMap.put("receiver", JacksonUtils.obj2Json(receiverMap));
        try {
            String sign = WXPayUtil.generateSignature(paramMap, payKey, WXPayConstants.SignType.HMACSHA256);
            paramMap.put("sign", sign);
            log.info(">>>>>> 普通直连分账接口｜删除分账接收方API. 签名结束之后：{}", JacksonUtils.obj2Json(paramMap));

            WxPayMyConfig wxMyConfig = new WxPayMyConfig();
            wxMyConfig.setAppId(appId);
            wxMyConfig.setKey(payKey);
            wxMyConfig.setMchID(mchId);

            WXPay wxPay = new WXPay(wxMyConfig);

            // 调用请求单次分账接口，并接受返回的结果
            String result = wxPay.requestWithoutCert(WeChatConfig.PROFIT_SHARING_REMOVE_RECEIVER_URL, paramMap,
                    WeChatConfig.TIME_OUT_MS, WeChatConfig.TIME_OUT_MS);
            log.info(">>>>>> 普通直连分账接口｜删除分账接收方API. 返回结果：{}", result);

            Map<String, String> res = WXPayUtil.xmlToMap(result);
            String returnCode = res.get("return_code");
            String resultCode = res.get("result_code");
            if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode))
                return true;
        } catch (Exception e) {
            log.error(">>>>>> 普通直连分账接口｜删除分账接收方API. ", e);
        }
        return false;
    }
}

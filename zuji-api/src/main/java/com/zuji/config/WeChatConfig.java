package com.zuji.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信参数配置
 *
 * @author Ink足迹
 * @create 2020-06-30 15:05
 **/
@Component
public class WeChatConfig {

    /**
     * 微信商户·收款账号证书位置
     */
    private static String RECEIVE_ACCOUNT_CERT_PATH;

    /**
     * 微信商户·收款账号ID
     */
    private static String RECEIVE_ACCOUNT_MCH_ID;

    /**
     * 微信商户·收款账号Key
     */
    private static String RECEIVE_ACCOUNT_PAY_KEY;

    /**
     * 微信商户·收款支付成功回调路径
     */
    private static String RECEIVE_ACCOUNT_PAY_NOTIFY_URL;

    /**
     * 网站网页 appId
     */
    private static String WEBSITE_APP_ID;

    /**
     * 网站网页 appSecret
     */
    private static String WEBSITE_APP_SECRET;

    /**
     * 服务号/公众号 appId
     */
    private static String NO_PUBLIC_APP_ID;

    /**
     * 服务号/公众号 appSecret
     */
    private static String NO_PUBLIC_APP_SECRET;


    /**
     * 授权域，弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息
     */
    public static final String SNSAPI_USERINFO = "snsapi_userinfo";

    /**
     * 不弹出授权页面，直接跳转，只能获取用户openid
     */
    public static final String SNSAPI_BASE = "snsapi_base";


    /**
     * 签名方式，固定值
     */
    public static final String PAY_SIGN_TYPE = "MD5";

    /**
     * 交易类型，小程序支付的固定值为JSAPI
     */
    public static final String PAY_TRADE_TYPE = "JSAPI";

    /**
     * H5支付的交易类型为MWEB
     */
    public static final String H5_PAY_TRADE_TYPE = "MWEB";

    /**
     * 分账签名类型
     */
    public static final String PROFIT_SHARING_SIGN_TYPE = "HMAC-SHA256";

    /**
     * 分账接收方类型:商户ID
     */
    public static final String PROFIT_SHARING_MERCHANT_ID = "MERCHANT_ID";

    /**
     * 分账接收方类型:个人openid
     */
    public static final String PROFIT_SHARING_PERSONAL_OPENID = "PERSONAL_OPENID";


    /**
     * 微信API域名
     */
    private static final String API_WX_DOMAIN_URL = "https://api.weixin.qq.com";

    /**
     * 微信page域名
     */
    private static final String OPEN_WX_DOMAIN_URL = "https://open.weixin.qq.com";

    /**
     * 通过code获取access_token,路径
     */
    public static final String OAUTH2_URL = API_WX_DOMAIN_URL + "/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

    /**
     * 获取个人用户信息路径
     */
    public static final String USER_INFO_URL = API_WX_DOMAIN_URL + "/sns/userinfo?access_token={0}&openid={1}&lang=zh_CN";

    /**
     * 服务号/公众号用户授权路径
     */
    public static String NO_PUBLIC_AUTHORIZED_URL = OPEN_WX_DOMAIN_URL + "/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope={2}&state={3}#wechat_redirect";

    /**
     * 全局唯一接口调用凭据,获取 access_token,请求路径
     */
    public final static String GLOBAL_TOKEN_URL = API_WX_DOMAIN_URL + "/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    /**
     * JS-SDK
     */
    public final static String JS_API_TICKET_URL = API_WX_DOMAIN_URL + "/cgi-bin/ticket/getticket?access_token={0}&type=jsapi";

    /**
     * 发送模板消息
     */
    public final static String MESSAGE_TEMPLATE_SEND_URL = API_WX_DOMAIN_URL + "/cgi-bin/message/template/send?access_token={0}";

    /**
     * 普通直连分账接口｜请求单次分账,请求Url
     */
    public static final String PROFIT_SHARING_URL = "https://api.mch.weixin.qq.com/secapi/pay/profitsharing";

    /**
     * 普通直连分账接口｜添加分账接收方,请求Url
     */
    public static final String PROFIT_SHARING_ADD_RECEIVER_URL = "https://api.mch.weixin.qq.com/pay/profitsharingaddreceiver";

    /**
     * 普通直连分账接口｜删除分账接收方,请求Url
     */
    public static final String PROFIT_SHARING_REMOVE_RECEIVER_URL = "https://api.mch.weixin.qq.com/pay/profitsharingremovereceiver";

    /**
     * 超时时间(单位：毫秒)
     */
    public static final int TIME_OUT_MS = 6000;

    /**
     * 编码格式
     */
    public static final String INPUT_CHARSET = "UTF-8";

    /**
     * 微信那边的编码格式
     */
    public static final String WX_INFO_CHARSET = "ISO-8859-1";

    /**
     * 支付结果通知返回
     */
    public static final String WX_PAY_RESULT_NOTIFY = "<xml>" +
            "<return_code><![CDATA[{0}]]></return_code>" +
            "<return_msg><![CDATA[{1}]]></return_msg>" +
            "</xml>";

    /**
     * 失败
     */
    public static final String FAIL = "FAIL";

    /**
     * 成功
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * OK
     */
    public static final String OK = "OK";


    @Value("${banma.wechat.receiveAccount.certPath}")
    public void setReceiveAccountCertPath(String receiveAccountCertPath) {
        RECEIVE_ACCOUNT_CERT_PATH = receiveAccountCertPath;
    }

    @Value("${banma.wechat.receiveAccount.mchId}")
    public void setReceiveAccountMchId(String receiveAccountMchId) {
        RECEIVE_ACCOUNT_MCH_ID = receiveAccountMchId;
    }

    @Value("${banma.wechat.receiveAccount.payKey}")
    public void setReceiveAccountPayKey(String receiveAccountPayKey) {
        RECEIVE_ACCOUNT_PAY_KEY = receiveAccountPayKey;
    }

    @Value("${banma.wechat.receiveAccount.payNotifyUrl}")
    public void setReceiveAccountPayNotifyUrl(String receiveAccountPayNotifyUrl) {
        RECEIVE_ACCOUNT_PAY_NOTIFY_URL = receiveAccountPayNotifyUrl;
    }

    @Value("${banma.wechat.wxWebsite.appId}")
    public void setWebsiteAppId(String websiteAppId) {
        WEBSITE_APP_ID = websiteAppId;
    }

    @Value("${banma.wechat.wxWebsite.appSecret}")
    public void setWebsiteAppSecret(String websiteAppSecret) {
        WEBSITE_APP_SECRET = websiteAppSecret;
    }

    @Value("${banma.wechat.noPublic.appId}")
    public void setNoPublicAppId(String noPublicAppId) {
        NO_PUBLIC_APP_ID = noPublicAppId;
    }

    @Value("${banma.wechat.noPublic.appSecret}")
    public void setNoPublicAppSecret(String noPublicAppSecret) {
        NO_PUBLIC_APP_SECRET = noPublicAppSecret;
    }

    public static String getReceiveAccountCertPath() {
        return RECEIVE_ACCOUNT_CERT_PATH;
    }

    public static String getReceiveAccountMchId() {
        return RECEIVE_ACCOUNT_MCH_ID;
    }

    public static String getReceiveAccountPayKey() {
        return RECEIVE_ACCOUNT_PAY_KEY;
    }

    public static String getReceiveAccountPayNotifyUrl() {
        return RECEIVE_ACCOUNT_PAY_NOTIFY_URL;
    }

    public static String getWebsiteAppId() {
        return WEBSITE_APP_ID;
    }

    public static String getWebsiteAppSecret() {
        return WEBSITE_APP_SECRET;
    }

    public static String getNoPublicAppId() {
        return NO_PUBLIC_APP_ID;
    }

    public static String getNoPublicAppSecret() {
        return NO_PUBLIC_APP_SECRET;
    }
}

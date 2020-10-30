package com.zuji.wechat.utils;

import com.zuji.config.WeChatConfig;
import com.zuji.wechat.bo.AuthorizeLoginByCodeRspBo;
import com.zuji.wechat.bo.AuthorizeUserInfoRspBo;
import com.zuji.wechat.bo.GlobalTokenRspBo;
import com.zuji.wechat.bo.JsApiTicketRspBo;
import com.zuji.common.util.JacksonUtils;
import com.zuji.common.util.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.MessageFormat;

/**
 * 微信相关方法
 *
 * @author Ink足迹
 * @create 2020-06-15 15:36
 **/
@Component
@Slf4j
public class WeChatPortUtil {
    /**
     * 通过 code 获取用户登录信息 扫码登录获取用户登录信息
     *
     * @param code 登录码
     * @return 返回结果
     */
    public static AuthorizeLoginByCodeRspBo getWebSiteUserAccessTokenByCode(String code) {
        return getAuthorizeLoginInfoByCode(WeChatConfig.OAUTH2_URL, WeChatConfig.getWebsiteAppId(), WeChatConfig.getWebsiteAppSecret(), code);
    }

    /**
     * 通过 code 获取用户登录信息 服务号/公众号
     *
     * @param code 登录码
     * @return 返回结果
     */
    public static AuthorizeLoginByCodeRspBo getNoPublicUserAccessTokenByCode(String code) {
        return getAuthorizeLoginInfoByCode(WeChatConfig.OAUTH2_URL, WeChatConfig.getNoPublicAppId(), WeChatConfig.getNoPublicAppSecret(), code);
    }

    /**
     * 获取授权登录信息，by：code
     *
     * @param url       请求路径
     * @param appId     网站网页/服务号/公众号的唯一标识
     * @param appSecret 网站网页/服务号/公众号的appSecret
     * @param code      前端获取的code
     * @return 返回结果
     */
    private static AuthorizeLoginByCodeRspBo getAuthorizeLoginInfoByCode(String url, String appId,
                                                                         String appSecret, String code) {
        String reqUrl = MessageFormat.format(url, appId, appSecret, code);
        String result = RestUtil.get(reqUrl);
        log.info(">>>>>> 微信获取登录授权信息. 通过code获取accessToken. result = {}", result);
        return JacksonUtils.json2Obj(result, AuthorizeLoginByCodeRspBo.class);
    }

    /**
     * 获取个人用户信息 网站网页/服务号/公众号
     *
     * @param accessToken 凭证
     * @param openId      普通用户的标识，对当前开发者帐号唯一
     * @return 返回结果
     */
    public static AuthorizeUserInfoRspBo getAuthorizeUserInfoByOpenId(String accessToken, String openId) {
        String reqUrl = MessageFormat.format(WeChatConfig.USER_INFO_URL, accessToken, openId);
        String result = RestUtil.get(reqUrl);
        log.info(">>>>>> 微信授权信息,获取用户登录个人信息. 解码前. result = {}", result);
        try {
            result = new String(result.getBytes(WeChatConfig.WX_INFO_CHARSET), WeChatConfig.INPUT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info(">>>>>> 微信授权信息,获取用户登录个人信息. 解码后. result = {}", result);
        return JacksonUtils.json2Obj(result, AuthorizeUserInfoRspBo.class);
    }

    /**
     * 获取 服务号 的全局 access_token
     *
     * @return 返回结果
     */
    public static GlobalTokenRspBo getNoPublicBaseAccessToken() {
        return getBaseAccessToken(WeChatConfig.getNoPublicAppId(), WeChatConfig.getNoPublicAppSecret());
    }

    /**
     * 向微信请求获取  全局 access_token
     *
     * @return 返回结果
     */
    private static GlobalTokenRspBo getBaseAccessToken(String appId, String appSecret) {
        // 请求获取 access_token
        String reqUrl = MessageFormat.format(WeChatConfig.GLOBAL_TOKEN_URL, appId, appSecret);
        String result = RestUtil.get(reqUrl);
        log.info(">>>>>> 微信授权信息,获取全局accessToken. result = {}", result);
        return JacksonUtils.json2Obj(result, GlobalTokenRspBo.class);
    }

    /**
     * 获取全局的jsapi_ticket
     *
     * @return 返回JsApiTicketRspBo
     */
    public static JsApiTicketRspBo getJsApiTicket(String accessToken) {
        String reqUrl = MessageFormat.format(WeChatConfig.JS_API_TICKET_URL, accessToken);
        String result = RestUtil.get(reqUrl);
        return JacksonUtils.json2Obj(result, JsApiTicketRspBo.class);
    }

    /**
     * SHA1 加密算法
     *
     * @param str 参数
     * @return 返回结果
     */
    public static String SHA1(String str) {
        try {
            //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

package com.zuji.wechat.utils;

import com.zuji.wechat.bo.GlobalTokenRspBo;
import com.zuji.wechat.bo.JsApiTicketRspBo;
import com.zuji.common.util.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 微信相关方法
 *
 * @author Ink足迹
 * @create 2020-06-24 17:38
 **/
@Component
public class WeChatRedisUtil {
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取服务号基础信息
     *
     * @return 返回结果
     */
    public String getNoPublicAccessToken() {
        String redisKey = "RedisKeyEnum.NO_PUBLIC_WECHAT_ACCESS_TOKEN.getKey()";
        if (redisUtil.hashKey(redisKey)) {
            return Objects.toString(redisUtil.get(redisKey));
        }
        GlobalTokenRspBo globalTokenRspBo = WeChatPortUtil.getNoPublicBaseAccessToken();
        if (!globalTokenRspBo.isSuccess()) {
            return null;
        }
        String accessToken = globalTokenRspBo.getAccessToken();
        int expiresIn = globalTokenRspBo.getExpiresIn();
        redisUtil.set(redisKey, accessToken, expiresIn - 600);
        return accessToken;
    }

    /**
     * 授权页ticket
     *
     * @return 返回结果
     */
    public String getJsApiTicket(String accessToken) {
        String redisKey = "RedisKeyEnum.NO_PUBLIC_WECHAT_JS_API_TICKET.getKey()";
        if (redisUtil.hashKey(redisKey)) {
            return Objects.toString(redisUtil.get(redisKey));
        }
        JsApiTicketRspBo jsApiTicket = WeChatPortUtil.getJsApiTicket(accessToken);
        if (!jsApiTicket.isSuccess()) {
            return null;
        }
        String ticket = jsApiTicket.getTicket();
        int expiresIn = jsApiTicket.getExpiresIn();
        redisUtil.set(redisKey, ticket, expiresIn - 600);
        return ticket;
    }
}

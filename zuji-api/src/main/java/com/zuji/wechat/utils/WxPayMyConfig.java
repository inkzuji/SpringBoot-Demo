package com.zuji.wechat.utils;

import com.zuji.config.WeChatConfig;
import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 微信相关参数配置
 *
 * @author Ink-足迹
 * @create 2018-05-23 16:38
 **/
public class WxPayMyConfig implements WXPayConfig {

    private byte[] certData;

    public WxPayMyConfig() throws Exception {
        File file = new File(WeChatConfig.getReceiveAccountCertPath());
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    private String mchID;

    private String key;

    private String appId;

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String getAppID() {
        return this.appId;
    }



    @Override
    public String getMchID() {
        return mchID;
    }

    public void setMchID(String mchID) {
        this.mchID = mchID;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 8000;
    }
}

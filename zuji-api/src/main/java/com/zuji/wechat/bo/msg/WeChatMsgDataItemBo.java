package com.zuji.wechat.bo.msg;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 微信模板消息-填充内容格式
 * @author hanganglin
 * @version create：2020/8/8 3:53 下午
 */
@Data
@AllArgsConstructor
public class WeChatMsgDataItemBo {

    /**
     * 模板填充内容
     */
    private String value;

    /**
     * 内容颜色
     */
    private String color = "#173177";

    public WeChatMsgDataItemBo(String value) {
        this.value = value;
    }

}

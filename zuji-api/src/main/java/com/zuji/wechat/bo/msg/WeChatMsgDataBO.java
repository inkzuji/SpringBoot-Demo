package com.zuji.wechat.bo.msg;

import lombok.Data;

/**
 * 通用的内容项
 * @author hanganglin
 * @version create：2020/8/8 3:57 下午
 */
@Data
public class WeChatMsgDataBO {

    /**
     * 首行
     */
    private WeChatMsgDataItemBo first;

    /**
     * 尾行
     */
    private WeChatMsgDataItemBo remark;

    /**
     * 栏目1
     */
    private WeChatMsgDataItemBo keyword1;

    /**
     * 栏目2
     */
    private WeChatMsgDataItemBo keyword2;

    /**
     * 栏目3
     */
    private WeChatMsgDataItemBo keyword3;

    /**
     * 栏目4
     */
    private WeChatMsgDataItemBo keyword4;

    /**
     * 栏目5
     */
    private WeChatMsgDataItemBo keyword5;

}

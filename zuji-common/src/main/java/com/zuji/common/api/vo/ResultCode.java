package com.zuji.common.api.vo;

import org.apache.commons.lang.StringUtils;

public enum ResultCode {
    SUCCESS(200, "success"),
    CODE_404(404, "路径不存在，请检查路径是否正确"),
    CODE_405(405, "不支持方法"),
    CODE_406(406, "空指针异常"),
    FAIL(500, "fail"),
    CODE_501(501, "数据库中已存在该记录"),
    CODE_502(502, "没有权限，请联系管理员授权"),
    CODE_503(503, "文件大小超出10MB限制, 请压缩或降低文件质量! "),
    CODE_504(504, "字段太长,超出数据库字段的长度"),
    CODE_505(505, "Redis 连接异常!"),
    CODE_506(506, "非法参数"),
    CODE_507(507, "参数解析异常"),
    CODE_508(508, "参数转换异常"),

    SYS_ERROR(-1, "系统繁忙");


    private Integer errCode;
    private String errMsg;

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    ResultCode(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    /**
     * 获取token相关异常
     *
     * @param msg 异常内容
     * @return 返回结果
     * @title getErrCodeByMsg
     * @modifyDate 2020-08-01
     * @modifyUser Ink足迹
     * @createDate 2020-08-01
     * @createUser Ink足迹
     */
    public static int getErrCodeByMsg(String msg) {
        // TODO: 2020/10/9 此处自行定义， 
        return 500;
    }

}

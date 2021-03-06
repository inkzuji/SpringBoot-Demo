package com.zuji.common.util.superSearch;


import com.zuji.common.util.LConvertUtils;

public enum QueryRuleEnum {

    GT(">", "大于"),
    GE(">=", "大于等于"),
    LT("<", "小于"),
    LE("<=", "小于等于"),
    EQ("=", "等于"),
    NE("!=", "不等于"),
    IN("IN", "包含"),
    LIKE("LIKE", "全模糊"),
    LEFT_LIKE("LEFT_LIKE", "左模糊"),
    RIGHT_LIKE("RIGHT_LIKE", "右模糊"),
    SQL_RULES("EXTEND_SQL", "自定义SQL片段");

    private String value;

    private String msg;

    QueryRuleEnum(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static QueryRuleEnum getByValue(String value) {
        if (LConvertUtils.isEmpty(value)) {
            return null;
        }
        for (QueryRuleEnum val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}

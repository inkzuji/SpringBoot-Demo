package com.zuji.wechat.bo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zuji.common.util.JacksonUtils;

import java.util.HashMap;

/**
 * 微信信息模板
 */
public class TemplateDataBo {

    private String touser;
    private String template_id;
    private String url;
    private String page;
    private String form_id;
    private String color;
    private TemplateItem data;
    private Miniprogram miniprogram;
    private String emphasis_keyword;

    public static TemplateDataBo newInstance() {
        return new TemplateDataBo();
    }

    private TemplateDataBo() {
        this.data = new TemplateItem();
        this.miniprogram = new Miniprogram();
    }

    public String getTouser() {
        return touser;
    }

    public TemplateDataBo setTouser(String touser) {
        this.touser = touser;
        return this;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public TemplateDataBo setTemplate_id(String template_id) {
        this.template_id = template_id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public TemplateDataBo setUrl(String url) {
        this.url = url;
        return this;
    }


    public TemplateItem getData() {
        return data;
    }

    public String getPage() {
        return page;
    }

    public TemplateDataBo setPage(String page) {
        this.page = page;
        return this;
    }

    public String getForm_id() {
        return form_id;
    }

    public TemplateDataBo setForm_id(String form_id) {
        this.form_id = form_id;
        return this;
    }

    public String getColor() {
        return color;
    }

    public TemplateDataBo setColor(String color) {
        this.color = color;
        return this;
    }

    public TemplateDataBo setMiniprogramAppid(String appid) {
        this.miniprogram.put("appid", appid);
        return this;
    }

    public TemplateDataBo setMiniprogramPagepath(String pagepath) {
        this.miniprogram.put("pagepath", pagepath);
        return this;
    }


    public TemplateDataBo add(String key, String value) {
        data.put(key, new Item(value, ""));
        return this;
    }

    public TemplateDataBo add(String key, String value, String color) {
        data.put(key, new Item(value, color));
        return this;
    }

    /**
     * 直接转化成jsonString
     *
     * @return {String}
     */
    public HashMap<String, Object> build() {
        return JacksonUtils.obj2Obj(this, new TypeReference<HashMap<String, Object>>() {
        });
    }


    public Miniprogram getMiniprogram() {
        return miniprogram;
    }

    public String getEmphasis_keyword() {
        return emphasis_keyword;
    }

    public TemplateDataBo setEmphasis_keyword(String emphasis_keyword) {
        this.emphasis_keyword = emphasis_keyword;
        return this;
    }

    private class TemplateItem extends HashMap<String, Item> {

        private static final long serialVersionUID = -3728490424738325020L;

        public TemplateItem() {
        }

        public TemplateItem(String key, Item item) {
            this.put(key, item);
        }
    }

    private class Miniprogram extends HashMap<String, String> {

        private static final long serialVersionUID = -3728490424738325021L;

        public Miniprogram() {
        }

    }


    private class Item {
        private Object value;
        private String color;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Item(Object value, String color) {
            this.value = value;
            this.color = color;
        }
    }
}


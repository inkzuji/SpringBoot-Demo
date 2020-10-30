package com.zuji.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.FatalBeanException;

import java.io.IOException;
import java.util.Objects;

/**
 * json转换
 *
 * @author Ink足迹
 * @create 2020-06-02 14:11
 **/
@Slf4j
public class JacksonUtils {
    private final static ObjectMapper mapper = new ObjectMapper();

    static {
        // 对于空的对象转json的时候不抛出错误
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 允许属性名称没有引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 设置输出时包含属性的风格
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 序列化，将对象转化为json字符串
     *
     * @param data 待转对象
     * @return 返回结果
     */
    public static String obj2Json(Object data) {
        if (Objects.isNull(data)) {
            return null;
        }
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("[{}] toJsonString error：{{}}", data.getClass().getSimpleName(), e);
            throw new FatalBeanException("obj to json exception");
        }
    }

    /**
     * 反序列化，将json字符串转化为对象
     *
     * @param json   待转对象
     * @param tClass 名称
     * @param <T>    类型
     * @return 返回结果
     */
    public static <T> T json2Obj(String json, Class<T> tClass) {
        if (json == null)
            return null;
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            log.error(" parse json [{}] to class [{}] error：{{}}", json, tClass.getSimpleName(), e);
            throw new FatalBeanException("json to obj exception");
        }
    }

    /**
     * 实体类相互转换
     *
     * @param data          待转换数据
     * @param typeReference 转换类型
     * @return 返回结果
     */
    public static <T> T obj2Obj(Object data, TypeReference<T> typeReference) {
        try {
            String json = mapper.writeValueAsString(data);
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            log.error(">>>>> bean[{}] -> bean[{}] error", data.getClass(), typeReference.getType().getTypeName(), e);
            throw new FatalBeanException("obj to obj exception");
        }
    }

    /**
     * json to jsonNode
     *
     * @param json 字符串
     * @return 返回 jsonNode
     */
    public static JsonNode json2Tree(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            log.error(">>>>> json[{}}] -> JsonNode error", json, e);
            throw new FatalBeanException("json to tree exception");
        }
    }

}

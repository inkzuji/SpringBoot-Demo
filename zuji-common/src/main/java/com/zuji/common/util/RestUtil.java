package com.zuji.common.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


public class RestUtil {

    /**
     * RestAPI 调用器
     */
    private final static RestTemplate RT = new RestTemplate();

    public static RestTemplate getRestTemplate() {
        return RT;
    }

    /**
     * 发送 get 请求
     */
    public static String get(String url) {
        return getNative(url, null, null).getBody();
    }

    /**
     * 发送 get 请求
     */
    public static String get(String url, HashMap<String, Object> variables) {
        return getNative(url, variables, null).getBody();
    }

    /**
     * 发送 get 请求
     */
    public static String get(String url, HashMap<String, Object> variables, HashMap<String, Object> params) {
        return getNative(url, variables, params).getBody();
    }

    /**
     * 发送 get 请求，返回原生 ResponseEntity 对象
     */
    public static ResponseEntity<String> getNative(String url, HashMap<String, Object> variables, HashMap<String, Object> params) {
        return request(url, HttpMethod.GET, variables, params);
    }

    /**
     * 发送 Post 请求
     */
    public static String post(String url) {
        return postNative(url, null, null).getBody();
    }

    /**
     * 发送 Post 请求
     */
    public static String post(String url, HashMap<String, Object> params) {
        return postNative(url, null, params).getBody();
    }

    /**
     * 发送 Post 请求
     */
    public static String post(String url, HashMap<String, Object> variables, HashMap<String, Object> params) {
        return postNative(url, variables, params).getBody();
    }

    /**
     * 发送 POST 请求，返回原生 ResponseEntity 对象
     */
    public static ResponseEntity<String> postNative(String url, HashMap<String, Object> variables, HashMap<String, Object> params) {
        return request(url, HttpMethod.POST, variables, params);
    }

    /**
     * 发送 put 请求
     */
    public static String put(String url) {
        return putNative(url, null, null).getBody();
    }

    /**
     * 发送 put 请求
     */
    public static String put(String url, HashMap<String, Object> params) {
        return putNative(url, null, params).getBody();
    }

    /**
     * 发送 put 请求
     */
    public static String put(String url, HashMap<String, Object> variables, HashMap<String, Object> params) {
        return putNative(url, variables, params).getBody();
    }

    /**
     * 发送 put 请求，返回原生 ResponseEntity 对象
     */
    public static ResponseEntity<String> putNative(String url, HashMap<String, Object> variables, HashMap<String, Object> params) {
        return request(url, HttpMethod.PUT, variables, params);
    }

    /**
     * 发送 delete 请求
     */
    public static String delete(String url) {
        return deleteNative(url, null, null).getBody();
    }

    /**
     * 发送 delete 请求
     */
    public static String delete(String url, HashMap<String, Object> variables, HashMap<String, Object> params) {
        return deleteNative(url, variables, params).getBody();
    }

    /**
     * 发送 delete 请求，返回原生 ResponseEntity 对象
     */
    public static ResponseEntity<String> deleteNative(String url, HashMap<String, Object> variables, HashMap<String, Object> params) {
        return request(url, HttpMethod.DELETE, null, variables, params, String.class);
    }

    /**
     * 发送请求
     */
    public static ResponseEntity<String> request(String url, HttpMethod method, HashMap<String, Object> variables, HashMap<String, Object> params) {
        return request(url, method, getHeaderApplicationJson(), variables, params, String.class);
    }

    /**
     * 发送请求
     *
     * @param url          请求地址
     * @param method       请求方式
     * @param headers      请求头  可空
     * @param variables    请求url参数 可空
     * @param params       请求body参数 可空
     * @param responseType 返回类型
     * @return ResponseEntity<responseType>
     */
    public static <T> ResponseEntity<T> request(String url, HttpMethod method, HttpHeaders headers,
                                                HashMap<String, Object> variables, HashMap<String, Object> params,
                                                Class<T> responseType) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("url 不能为空");
        }
        if (method == null) {
            throw new RuntimeException("method 不能为空");
        }
        if (headers == null) {
            headers = new HttpHeaders();
        }
        // 请求体
        String body = "";
        if (params != null) {
            body = JacksonUtils.obj2Json(params);
        }
        // 拼接 url 参数
        if (variables != null) {
            url += ("?" + asUrlVariables(variables));
        }
        // 发送请求
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        return RT.exchange(url, method, request, responseType);
    }

    /**
     * 获取JSON请求头
     */
    private static HttpHeaders getHeaderApplicationJson() {
        return getHeader(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    /**
     * 获取请求头
     */
    private static HttpHeaders getHeader(String mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mediaType));
        headers.add("Accept", mediaType);
        return headers;
    }

    /**
     * 将 HashMap 转为 a=1&b=2&c=3...&n=n 的形式
     */
    public static String asUrlVariables(HashMap<String, Object> variables) {
        StringBuffer urlVariables = new StringBuffer();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String key = entry.getKey();
            String value = "";
            Object object = entry.getValue();
            if (object != null && !StringUtils.isNotBlank(object.toString()))
                value = object.toString();
            if (urlVariables.length() > 0)
                urlVariables.append("&");
            urlVariables.append(key).append("=").append(value);
        }
        return urlVariables.toString();

    }

}

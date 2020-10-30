package com.zuji.config.oss;

import com.zuji.common.util.oss.OssBootUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssBootConfiguration {

    @Value("${banma.oss.endpoint}")
    private String endpoint;
    @Value("${banma.oss.outerEndpoint}")
    private String outerEndpoint;
    @Value("${banma.oss.accessKey}")
    private String accessKeyId;
    @Value("${banma.oss.secretKey}")
    private String accessKeySecret;
    @Value("${banma.oss.bucketName}")
    private String bucketName;
    @Value("${banma.oss.staticDomain}")
    private String staticDomain;

    @Bean
    public void initOssBootConfiguration() {
        new OssBootUtil.Builder(endpoint, outerEndpoint, accessKeyId, accessKeySecret, bucketName, staticDomain)
                .build();
    }
}
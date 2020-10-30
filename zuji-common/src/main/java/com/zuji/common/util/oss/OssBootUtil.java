package com.zuji.common.util.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.LifecycleRule;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.SetBucketLifecycleRequest;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.UUID;


/**
 * 足迹
 */
public class OssBootUtil {
    private final static Logger LOG = LoggerFactory.getLogger(OssBootUtil.class);

    private static String END_POINT;
    private static String OUTER_ENDPOINT;
    private static String ACCESS_KEY_ID;
    private static String ACCESS_KEY_SECRET;
    private static String BUCKET_NAME;
    private static String STATIC_DOMAIN;

    private OssBootUtil(Builder builder) {
        END_POINT = builder.endPoint;
        OUTER_ENDPOINT = builder.outerEndpoint;
        ACCESS_KEY_ID = builder.accessKeyId;
        ACCESS_KEY_SECRET = builder.accessKeySecret;
        BUCKET_NAME = builder.bucketName;
        STATIC_DOMAIN = builder.staticDomain;
    }

    public static class Builder {
        String endPoint;
        String outerEndpoint;
        String accessKeyId;
        String accessKeySecret;
        String bucketName;
        String staticDomain;

        public Builder(String endPoint, String outerEndpoint, String accessKeyId, String accessKeySecret,
                       String bucketName, String staticDomain) {
            this.endPoint = endPoint;
            this.outerEndpoint = outerEndpoint;
            this.accessKeyId = accessKeyId;
            this.accessKeySecret = accessKeySecret;
            this.bucketName = bucketName;
            this.staticDomain = staticDomain;
        }

        public OssBootUtil build(){
            return new OssBootUtil(this);
        }
    }


    /**
     * 初始化 oss 客户端
     */
    private static OSS initOSS() {
        return new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 上传文件至阿里云 OSS 文件上传成功,返回文件完整访问路径 文件上传失败,返回 null
     *
     * @param file    待上传文件
     * @param fileDir 文件保存目录
     * @return oss 中的相对文件路径
     */
    public static String upload(MultipartFile file, String fileDir) {
        OSS ossClient = initOSS();
        StringBuilder fileUrl = new StringBuilder();
        String FILE_URL = null;
        try {
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;

            fileDir = !fileDir.endsWith("/") ? fileDir.concat("/") : fileDir;

            fileUrl = fileUrl.append(STATIC_DOMAIN).append(fileDir + fileName);

            FILE_URL = MessageFormat.format("https://{0}.{1}/{2}", BUCKET_NAME, OUTER_ENDPOINT, fileUrl);
            PutObjectResult result = ossClient.putObject(BUCKET_NAME, fileUrl.toString(), file.getInputStream());
            // 设置权限(公开读)
            ossClient.setBucketAcl(BUCKET_NAME, CannedAccessControlList.PublicRead);

            if (result != null) LOG.info("------OSS文件上传成功------, fileUrl = {}", fileUrl);

        } catch (IOException e) {
            LOG.error("上传图片失败", e);
        } finally {
            ossClient.shutdown();
        }
        return FILE_URL;
    }


    /**
     * 上传文件至阿里云 OSS 文件上传成功,返回文件完整访问路径 文件上传失败,返回 null
     *
     * @param file    待上传文件
     * @param fileDir 文件保存目录
     * @return oss 中的相对文件路径
     */
    public static String upload(FileItemStream file, String fileDir) {
        OSS ossClient = initOSS();
        StringBuilder fileUrl = new StringBuilder();
        String FILE_URL = null;
        try {
            String suffix = file.getName().substring(file.getName().lastIndexOf('.'));
            String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;
            fileDir = !fileDir.endsWith("/") ? fileDir.concat("/") : fileDir;
            fileUrl = fileUrl.append(fileDir).append(fileName);

            FILE_URL = MessageFormat.format("https://{0}.{1}/{2}", BUCKET_NAME, OUTER_ENDPOINT, fileUrl);
            PutObjectResult result = ossClient.putObject(BUCKET_NAME, fileUrl.toString(), file.openStream());
            // 设置权限(公开读)
            ossClient.setBucketAcl(BUCKET_NAME, CannedAccessControlList.PublicRead);

            if (result != null) LOG.info("------OSS文件上传成功------, fileUrl = {}", fileUrl);
        } catch (IOException e) {
            LOG.error("上传图片失败", e);
        } finally {
            ossClient.shutdown();
        }
        return FILE_URL;
    }

    /**
     * 自定义上传文件夹，文件名称
     *
     * @param inputStream 需上传文件内容
     * @param fileName    文件名，例：abc.jpg
     * @param definedDir  文件夹路径 xx/xxx/
     * @return 返回oss文件路径
     */
    public static String upload(InputStream inputStream, String fileName, String definedDir) {
        OSS ossClient = initOSS();
        try {
            StringBuffer fileUrl = new StringBuffer();
            fileUrl.append(STATIC_DOMAIN).append(definedDir).append(fileName);

            // 上传文件
            PutObjectResult result = ossClient.putObject(BUCKET_NAME, fileUrl.toString(), inputStream);
            if (result == null) return null;

            return MessageFormat.format("https://{0}.{1}/{2}", BUCKET_NAME, OUTER_ENDPOINT, fileUrl);
        } catch (Exception e) {
            LOG.error("上传图片失败", e);
        } finally {
            ossClient.shutdown();
        }
        return null;
    }


    /**
     * 删除文件
     */
    public static void deleteUrl(String url) {
        String bucketUrl = MessageFormat.format("https://{0}.{1}/", BUCKET_NAME, OUTER_ENDPOINT);
        url = url.replace(bucketUrl, "");
        OSS ossClient = initOSS();
        try {
            ossClient.deleteObject(BUCKET_NAME, url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 删除文件
     */
    public static void delete(String fileName) {
        OSS ossClient = initOSS();
        try {
            ossClient.deleteObject(BUCKET_NAME, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 配置生命周期
     *
     * @param ruleId      规则ID
     * @param marchPrefix 规则前缀
     * @param overDays    过期天数
     * @title settingOssLifecycleRule
     * @modifyDate 2020-07-16
     * @modifyUser Ink足迹
     * @createDate 2020-07-16
     * @createUser Ink足迹
     */
    public static void settingOssLifecycleRule(String ruleId, String marchPrefix, int overDays) {
        OSS ossClient = initOSS();
        try {
            // 创建SetBucketLifecycleRequest。
            SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(BUCKET_NAME);
            LifecycleRule rule = new LifecycleRule(ruleId, marchPrefix, LifecycleRule.RuleStatus.Enabled, overDays);
            request.AddLifecycleRule(rule);
            ossClient.setBucketLifecycle(request);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }
}
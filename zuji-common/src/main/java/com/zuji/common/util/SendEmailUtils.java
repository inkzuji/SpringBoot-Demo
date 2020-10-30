package com.zuji.common.util;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.NewsAddress;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * 发送电子邮件
 *
 * @author Ink足迹
 * @create 2019-11-08 14:31
 **/
@Component
@Slf4j
public class SendEmailUtils {

    /**
     * 登陆用户名
     */
    private final String sendAccount;

    /**
     * 登陆密码
     */
    private final String sendPassword;

    /**
     * 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
     */
    private final String host;

    /**
     * 端口
     */
    private final String port;

    /**
     * 协议
     */
    private final String protocol;

    public SendEmailUtils(Builder builder) {
        this.sendAccount = builder.sendAccount;
        this.sendPassword = builder.sendPassword;
        this.host = builder.host;
        this.port = builder.port;
        this.protocol = builder.protocol;
    }

    public static class Builder{
        private final String sendAccount;
        private final String sendPassword;
        private final String host;
        private final String port;
        private final String protocol;

        public Builder(String sendAccount, String sendPassword, String host, String port, String protocol) {
            this.sendAccount = sendAccount;
            this.sendPassword = sendPassword;
            this.host = host;
            this.port = port;
            this.protocol = protocol;
        }
        public SendEmailUtils build(){
            return new SendEmailUtils(this);
        }
    }

    public void sendEmail(EmailMsgVO emailMsgVO) {
        log.info(">>>>>>>function sendEmail(), send email start");
        // 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // 需要请求认证
        props.put("mail.smtp.auth", "true");

        // 开启 SSL 连接
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return;
        }
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        // 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);

        // 设置为debug模式, 可以查看详细的发送 log
        session.setDebug(false);

        try {
            // 创建邮件
            MimeMessage message = this.createMimeMessage(session, sendAccount, emailMsgVO);

            // 根据 Session 获取邮件传输对象
            Transport transport = session.getTransport();

            // 使用 邮箱账号 和 密码 连接邮件服务器
            //    这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
            transport.connect(sendAccount, sendPassword);

            // 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());

            // 关闭连接
            transport.close();
            log.info(">>>>>>>>>>>>function sendEmail, send email success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session    和服务器交互的会话
     * @param sendMail   发件人邮箱
     * @param emailMsgVO 邮件信息
     * @return
     * @throws Exception
     */
    private MimeMessage createMimeMessage(Session session, String sendMail, EmailMsgVO emailMsgVO) throws Exception {
        // 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // From: 发件人
        message.setFrom(new InternetAddress(sendMail, emailMsgVO.getSendName(), "UTF-8"));

        // To: 收件人（可以增加多个收件人、抄送、密送）
        // new InternetAddress(receiveMail, null, "UTF-8")
        message.setRecipient(MimeMessage.RecipientType.TO, new NewsAddress(emailMsgVO.getReceiveMailAccount()));

        // Subject: 邮件主题
        message.setSubject(emailMsgVO.getSubject(), "UTF-8");

        // Content: 邮件正文（可以使用html标签）
        message.setContent(emailMsgVO.getContent(), "text/html;charset=UTF-8");

        // 设置发件时间
        message.setSentDate(new Date());

        // 保存设置
        message.saveChanges();

        return message;
    }
}

class EmailMsgVO {
    /**
     * 接收人
     */
    private String receiveMailAccount;

    /**
     * 发件人名称
     */
    private String sendName;

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    public String getReceiveMailAccount() {
        return receiveMailAccount;
    }

    public void setReceiveMailAccount(String receiveMailAccount) {
        this.receiveMailAccount = receiveMailAccount;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "sendName='" + sendName +
                "', subject='" + subject +
                "', content='" + content + "'";
    }
}
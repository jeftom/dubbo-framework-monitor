package com.framework.mail;


import com.framework.tools.CommonPair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * 测试apache-commons-email 发邮件
 * Created by guoyongzheng on 15/3/28.
 */
public class MailServiceTest {

    /**
     * 测试发邮件
     */
//    @Test
//    public void testSendMail() throws EmailException {
//        Email email = new SimpleEmail();
//        email.setHostName("mail.yst.com.cn");
//        //email.setSmtpPort(25);
//        email.setAuthenticator(new DefaultAuthenticator("oa", "yst123!"));
//        //email.setSSLOnConnect(true);
//        email.setFrom("oa@mail.yst.com.cn");
//        email.setSubject("TestMail");
//        email.setMsg("This is a test mail ... :-)");
//        email.addTo("yzguo5@mail.yst.com.cn");
//        //email.addTo("gyongzheng@hotmail.com");
//        email.send();
//    }
    @Test
    public void testMailService() throws ExecutionException, InterruptedException {
        String from = "oa@mail.yst.com.cn";
        List<CommonPair<String, String>> to = new ArrayList<>();
        to.add(new CommonPair<>("yzguo5@mail.yst.com.cn", "郭永正"));
        CommonPair<String, String> authenticator = new CommonPair<>("oa", "yst123!");
        String subject = "TestMail";
        String msg = "This is a test...";
        Future<String> future = MailService.sendMail(from, to, authenticator, subject, msg);

        assertNotNull(future.get());
    }

    @Test
    public void testMailService_Html() throws ExecutionException, InterruptedException {
        String from = "oa@mail.yst.com.cn";
        List<CommonPair<String, String>> to = new ArrayList<>();
        to.add(new CommonPair<>("yzguo5@mail.yst.com.cn", "郭永正"));
        CommonPair<String, String> authenticator = new CommonPair<>("oa", "yst123!");
        String subject = "TestMail";
        String htmlMsg = "<html><body><table><tbody><tr><td>td1</td><td>td2</td></tr></tbody></table></body></html>";
        String textMsg = "如果邮件客户端不支持html格式，那么这段话会显示出来。";
        Future<String> future = MailService.sendHtmlMail(from, to, authenticator, subject, htmlMsg, textMsg);

        assertNotNull(future.get());
    }
}

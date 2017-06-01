package com.nfsq.framework.mail;

import com.nfsq.framework.tools.CommonPair;
import com.nfsq.framework.tools.FrameworkThreadPool;
import com.nfsq.framework.tools.WrappedRTException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 发送邮件服务
 * Created by guoyongzheng on 15/3/30.
 */
public class MailService {

    private static Log log = LogFactory.getLog(MailService.class);

    /**
     * 纯静态方法类，防止实例化
     */
    private MailService() {
    }

    /**
     * 发送普通邮件
     *
     * @param from          发件人地址
     * @param to            收件人地址（List of Pair［地址，名称］，如：List[Pair["yzguo5@mail.yst.com.cn","guoyongzheng"]]）
     * @param authenticator 账号验证 (Pair [用户名，密码]， 如：Pair["yzguo5", "yzguo5_password"])
     * @param subject       标题
     * @param msg           内容
     * @return
     * @throws EmailException
     */
    public static Future<String> sendMail(String from, List<CommonPair<String, String>> to, CommonPair<String, String> authenticator, String subject, String msg) {
        Future<String> future = FrameworkThreadPool.submit(() -> {
            try {
                Email email = new SimpleEmail();
                email.setHostName("mail.yst.com.cn");
                email.setAuthenticator(new DefaultAuthenticator(authenticator.getFirst(), authenticator.getSecond()));
                email.setFrom(from);
                email.setSubject(subject);
                email.setMsg(msg);

                int receivers = 0;
                for (CommonPair<String, String> pair : to) {
                    if (pair.getFirst() == null) {
                        log.error("至少一个邮件地址为空： " + to);
                        continue;
                    }
                    email.addTo(pair.getFirst(), pair.getSecond());
                    receivers++;
                }
                if (receivers == 0) {
                    log.error("该邮件没有收件人。 from= " + from + "; subject= " + subject +"; msg=" + msg);
                    return null;
                }

                return email.send();
            } catch (Exception t) {
                log.error(t);
                throw new WrappedRTException(t);
            }

        });

        return future;
    }

    /**
     * 发送HTML邮件
     *
     * @param from          发件人地址
     * @param to            收件人地址（List of Pair［地址，名称］，如：List[Pair["yzguo5@mail.yst.com.cn","guoyongzheng"]]）
     * @param authenticator 账号验证 (Pair [用户名，密码]， 如：Pair["yzguo5", "yzguo5_password"])
     * @param subject       标题
     * @param htmlMsg       html内容
     * @param textMsg       （如果邮件客户端不支持html，则显示：）文本内容
     * @return
     * @throws EmailException
     */
    public static Future<String> sendHtmlMail(String from, List<CommonPair<String, String>> to, CommonPair<String, String> authenticator, String subject, String htmlMsg, String textMsg) {
        Future<String> future = FrameworkThreadPool.submit(() -> {
            try {
                HtmlEmail email = new HtmlEmail();
                email.setHostName("mail.yst.com.cn");
                email.setAuthenticator(new DefaultAuthenticator(authenticator.getFirst(), authenticator.getSecond()));
                email.setFrom(from);
                email.setSubject(subject);
                email.setHtmlMsg(htmlMsg);
                email.setTextMsg(textMsg);

                for (CommonPair<String, String> pair : to) {
                    email.addTo(pair.getFirst(), pair.getSecond());
                }

                return email.send();
            } catch (Exception t) {
                log.error(t);
                throw new WrappedRTException(t);
            }

        });

        return future;
    }

}

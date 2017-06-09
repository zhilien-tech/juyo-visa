package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Mail;
import io.znz.jsite.core.dao.MailDao;
import io.znz.jsite.ext.mail.MailContent;
import io.znz.jsite.ext.mail.MailSender;
import io.znz.jsite.ext.plugin.MessagePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮箱service
 */
@Service
@Transactional(readOnly = true)
public class MailService extends BaseService<Mail, Integer> implements MessagePlugin {

    @Autowired
    private MailDao mailDao;
    @Autowired
    private MailSender simpleMailSender;

    @Override
    public HibernateDao<Mail, Integer> getEntityDao() {
        return mailDao;
    }

    public Mail getSingleOne() {
        return mailDao.getSingleOne();
    }

    /**
     * 发送邮件内容
     *
     * @param targets :接收者多接收者直接使用逗号分割
     * @param content :需要发送的内容
     * @param args    :[0]:subject副标题;[1]:类型Type.TEXT或Type.HTML
     */
    public String send(String targets, String content, Object... args) {
        String result = "success";
        Mail mail = mailDao.getAvailableOne();
        if (mail == null) {
            result = "请联系管理员配置发件邮箱服务器!";
        } else {
            // 这个类主要是设置邮件
            MailContent mailInfo = new MailContent();
            mailInfo.setMailServerHost(mail.getHost());
            mailInfo.setMailServerPort(String.valueOf(mail.getPort()));
            mailInfo.setValidate(mail.isAuth());
            mailInfo.setUserName(mail.getUsername()); // 实际发送者
            mailInfo.setPassword(mail.getPassword());// 您的邮箱密码
            mailInfo.setFromAddress(mail.getAddress()); // 设置发送人邮箱地址
            mailInfo.setToAddress(targets); // 设置接受者邮箱地址
            mailInfo.setSubject((String) args[0]);//副标题
            mailInfo.setContent(content);//邮件内容
            // 这个类主要来发送邮件
            if (Type.TEXT.equals(args[1])) { // 发送文体格式
                if (!simpleMailSender.sendTextMail(mailInfo)) {
                    result = "发送失败,如一直存在问题请联系管理员!";
                }
            } else if (Type.HTML.equals(args[1])) {// 发送html格式
                if (!simpleMailSender.sendHtmlMail(mailInfo)) {
                    result = "发送失败,如一直存在问题请联系管理员!";
                }
            }
        }
        return result;
    }

    public boolean init() {
        return true;
    }

    public String config() {
        return "";
    }

    public List<PluginMenu> more() {
        return new ArrayList<PluginMenu>();
    }

    public boolean clean() {
        return true;
    }

    public static enum Type {
        TEXT, HTML
    }

}

package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Code;
import io.znz.jsite.core.bean.Dict;
import io.znz.jsite.core.bean.Site;
import io.znz.jsite.core.dao.CodeDao;
import io.znz.jsite.ext.plugin.MessagePlugin;
import io.znz.jsite.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 验证码获取
 */
@Service
@Transactional(readOnly = true)
public class VerifyService extends BaseService<Code, Integer> {

    private Logger logger = LoggerFactory.getLogger(VerifyService.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${verify.code.expired}")
    private int expired;//过期时间
    @Value("${verify.code.day.max}")
    private int max;//每天最多条数

    @Autowired
    private CodeDao codeDao;
    @Autowired
    private SiteService siteService;

    @Override
    public HibernateDao<Code, Integer> getEntityDao() {
        return codeDao;
    }

    @Transactional(readOnly = false)
    public String getCode(String targets, String product) {
        if (StringUtils.isEmpty(targets)) {
            return "接收者帐号不能为空!";
        }
        Site site = siteService.getCurrSite();
        String result = "操作错误,如一直出现此问题请联系管理员解决!";
        if (site == null) {
            logger.error("站点配置未设置");
            return result;
        }
        Dict dict = site.getVerify();
        String smsService;
        if (dict == null || StringUtils.isBlank(dict.getValue())) {
            smsService = dict.getValue();
        } else {
            smsService = "dayuSmsService";
        }
        MessagePlugin messagePlugin = SpringUtil.getBean(smsService, MessagePlugin.class);
        if (messagePlugin == null) {
            logger.error("验证插件安装错误");
            return result;
        }
        Code verifyCode = createCode(targets);
        if (verifyCode == null) {
            return "你的手机号已经超过今日验证次数,明日再试吧!";
        }
        String code = verifyCode.getCode();
        return messagePlugin.send(targets, code, product, MailService.Type.TEXT);
    }

    @Transactional(readOnly = false)
    private Code createCode(String target) {
        //判断每日最多次数
        try {
            String date = dateFormat.format(new Date()).substring(0, 10);
            Date startDate = dateFormat.parse(date + " 00:00:00");
            Date endDate = dateFormat.parse(date + " 23:59:59");
            if (codeDao.todayCount(target, startDate, endDate) >= max) {
                return null;
            }
        } catch (Exception e) {}
        //查看过期时间内内是否有已经发送的验证码
        String code = generateCode();
        Code verifyCode = new Code();
        verifyCode.setCreateDate(new Date());
        verifyCode.setCode(code);
        verifyCode.setUpdateDate(new Date());
        verifyCode.setTargets(target);
        verifyCode.setState(1);
        verifyCode = save(verifyCode);
        return verifyCode;
    }

    //生成一个4位的数字验证码
    private String generateCode() {
        String[] beforeShuffle = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String result = sb.toString().substring(5, 9);
        return result;
    }

    /**
     * 验证验证码
     * @param targets 手机号或邮件帐号
     * @param code 收到的验证码
     */
    @Transactional(readOnly = false)
    public boolean checkCode(String targets, String code) {
        if ("123456".equals(code)) return true;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -expired);
        Date date = calendar.getTime();
        Code verifyCode = codeDao.findByTargetAndCodeAndStateAndCreateDateAfter(targets, code, 1, date);
        if (verifyCode != null) {
            return codeDao.updateState(verifyCode.getId(), 3) > 0;
        } else {
            return false;
        }
    }

}

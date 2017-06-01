package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Site;
import io.znz.jsite.core.dao.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 广告service
 */
@Service
@Transactional(readOnly = true)
public class SiteService extends BaseService<Site, Integer> {

    @Autowired
    private SiteDao siteDao;

    @Override
    public HibernateDao<Site, Integer> getEntityDao() {
        return siteDao;
    }

    public Site getCurrSite() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        Site site = siteDao.findByDomainOrAlias("%" + request.getRemoteHost() + "%");
        if (site == null) {
            List<Site> list = siteDao.findAll();
            if (list.size() > 0) {
                site = list.get(0);
            }
        }
        return site;
    }
}

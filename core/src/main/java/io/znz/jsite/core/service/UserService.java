package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.dao.UserDao;
import io.znz.jsite.ext.shiro.UsernamePasswordCaptchaToken;
import io.znz.jsite.util.DateUtils;
import io.znz.jsite.util.IPUtil;
import io.znz.jsite.util.security.Digests;
import io.znz.jsite.util.security.Encodes;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 用户service
 *
 * @author Chaly
 */
@Service
@Transactional(readOnly = true)
public class UserService extends BaseService<User, String> {

    /**
     * 加密方法
     */
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;    //盐长度

    @Autowired
    private UserDao userDao;

    @Override
    public HibernateDao<User, String> getEntityDao() {
        return userDao;
    }

    /**
     * 保存用户
     */
    @Transactional(readOnly = false)
    public User save(User user) {
        entryptPassword(user);
        user.setCreateDate(DateUtils.getSysTimestamp());
        return userDao.save(user);
    }

    /**
     * 修改密码
     */
    @Transactional(readOnly = false)
    public void updatePwd(User user) {
        entryptPassword(user);
        userDao.save(user);
    }

    /**
     * 删除用户
     */
    @Transactional(readOnly = false)
    public void delete(String id) {
        if (!isSupervisor(id))
            userDao.delete(id);
    }

    /**
     * 按登录名查询用户
     *
     * @return 用户对象
     */
    public User getUser(String loginName) {
        return userDao.findByLoginName(loginName);
    }

    /**
     * 判断是否超级管理员
     *
     * @return boolean
     */
    private boolean isSupervisor(String id) {
        return "1".equals(id);
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));
        byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }

    /**
     * 验证原密码是否正确
     */
    public boolean checkPassword(User user, String oldPassword) {
        byte[] salt = Encodes.decodeHex(user.getSalt());
        byte[] hashPassword = Digests.sha1(oldPassword.getBytes(), salt, HASH_INTERATIONS);
        if (user.getPassword().equals(Encodes.encodeHex(hashPassword))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 修改用户登录
     */
    public User updateUserLogin(User user) {
        user.setLoginCount((user.getLoginCount() == null ? 0 : user.getLoginCount()) + 1);
        user.setPreviousVisit(user.getLastVisit());
        user.setLastVisit(DateUtils.getSysTimestamp());
        update(user);
        return user;
    }

    /**
     * 修改用户昵称
     */
    @Transactional(readOnly = false)
    public void updateName(Integer id, String name) {
        userDao.updateName(id, name);
    }

    /**
     * 修改用户昵称
     */
    @Transactional(readOnly = false)
    public void updateLoginName(Integer id, String loginName) {
        userDao.updateLoginName(id, loginName);
    }

    public int countByDate(Date date) {
        return userDao.countByDate(date);
    }

    public List<User> findByDate(Date start, Date end) {
        return userDao.findByDate(start, end);
    }

    public boolean existLoginName(String loginName) {
        return userDao.existLoginName(loginName) > 0;
    }

    public void autoLogin(HttpServletRequest request, User user) {
        //直接使用密文密码登录
        UsernamePasswordCaptchaToken token = new UsernamePasswordCaptchaToken(
            user.getLoginName(), user.getPassword(), true, null,
            IPUtil.getIpAddress(request)
        );
        //设置支持密文直接登录
        token.setDirect(true);
        SecurityUtils.getSubject().login(token);
    }

    public List<User> searchByIdsAndName(List<String> ids, String keyword) {
        return userDao.searchByIdsAndName(ids, "%" + keyword + "%");
    }
}

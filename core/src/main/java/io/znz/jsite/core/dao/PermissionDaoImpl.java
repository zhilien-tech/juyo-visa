package io.znz.jsite.core.dao;

import io.znz.jsite.core.bean.Permission;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


/**
 * 权限DAO
 * @author Chaly
 */
@Repository
public class PermissionDaoImpl implements PermissionDaoExt<Permission> {

    @PersistenceContext
    private EntityManager em;

    /**
     * 查询用户拥有的权限
     * @param userId 用户id
     * @return 结果集合
     */
    @SuppressWarnings("unchecked")
    public List<Permission> findPermissions(String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select p.* from sys_permission p ");
        sb.append("INNER JOIN sys_role_permission rp on p.ID=rp.PERMISSION_ID ");
        sb.append("INNER JOIN sys_role r ON  r.id=rp.ROLE_ID ");
        sb.append("INNER JOIN sys_user_role  ur ON ur.ROLE_ID =rp.ROLE_ID ");
        sb.append("INNER JOIN sys_user u ON u.id = ur.USER_ID ");
        sb.append("where u.id=?0 order by p.sort");
        Query sqlQuery = em.createNativeQuery(sb.toString(), Permission.class);
        sqlQuery.setParameter(0, userId);
        return sqlQuery.getResultList();
    }

    /**
     * 查询所有的菜单
     * @return 菜单集合
     */
    public List<Permission> findMenus() {
        StringBuffer sb = new StringBuffer();
        sb.append("select p.* from sys_permission p where p.TYPE='F' order by p.sort");
        Query sqlQuery = em.createNativeQuery(sb.toString(), Permission.class);
        return sqlQuery.getResultList();
    }

    /**
     * 查询用户拥有的菜单权限
     * @return 结果集合
     */
    public List<Permission> findMenus(String uid, Integer pid) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT p.*,(SELECT if(count(p1.id)>0,'closed','open') FROM sys_permission p1 WHERE p1.pid = p.id AND p1.TYPE='F') AS state FROM sys_permission p ");
        sb.append("INNER JOIN sys_role_permission rp ON p.ID=rp.PERMISSION_ID ");
        sb.append("INNER JOIN sys_role r ON r.id=rp.ROLE_ID ");
        sb.append("INNER JOIN sys_user_role ur ON ur.ROLE_ID =rp.ROLE_ID ");
        sb.append("INNER JOIN sys_user u ON u.id = ur.USER_ID ");
        sb.append("where p.TYPE='F' AND u.id=?0 ");
        if (pid == null || pid == 0) {
            sb.append("AND p.pid IS NULL ");
        } else {
            sb.append("AND p.pid = ?1 ");
        }
        sb.append("ORDER BY p.sort");
        Query sqlQuery = em.createNativeQuery(sb.toString(), "PermissionResult");
        sqlQuery.setParameter(0, uid);
        if (pid != null && pid != 0) {
            sqlQuery.setParameter(1, pid);
        }
        List<Object[]> list = sqlQuery.getResultList();
        List<Permission> results = new ArrayList<Permission>();
        for (Object[] obj : list) {
            Permission p = (Permission) obj[0];
            p.setState((String) obj[1]);
            results.add(p);
        }
        list.clear();
        list = null;
        return results;
    }

    /**
     * 查询菜单下的操作权限
     */
    public List<Permission> findMenuOperation(Integer pid) {
        StringBuffer sb = new StringBuffer();
        sb.append("select p.* from sys_permission p where p.TYPE='O' and p.PID=?0 order by p.SORT");
        Query sqlQuery = em.createNativeQuery(sb.toString(), Permission.class);
        sqlQuery.setParameter(0, pid);
        return sqlQuery.getResultList();
    }

}

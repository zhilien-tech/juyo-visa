package io.znz.jsite.core.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class BackupDaoImpl implements BackupDao {

    @PersistenceContext
    private EntityManager entityManager;

    public String getDefaultCatalog() throws SQLException {
        Connection connection = entityManager.unwrap(SessionImpl.class).connection();
        return connection.getCatalog();
    }

    public List<String> listTables(String catalog) {
        // String sql = " show tables ";
        // TABLE_NAME
        String sql = " SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA='" + catalog + "' ";
        Query query = entityManager.createNativeQuery(sql);
        List<String> tables = query.getResultList();
        return tables;
    }


    public List<Map<String, String>> listFields(String tablename) {
        String sql = " SHOW FULL COLUMNS FROM   " + tablename;
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, String>> fields = query.getResultList();
        return fields;
    }

    public String createTableDDL(String tablename) {
        String sql = " show create table " + tablename;
        Query query = entityManager.createNativeQuery(sql);
        String ddl = (String) (((Object[]) query.getSingleResult())[1]);
        return ddl;
    }

    public List<Object[]> createTableData(String tablename) {
        String sql = " select * from   " + tablename;
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<String> listDataBases() {
        String sql = " show  databases ";
        Query query = entityManager.createNativeQuery(sql);
        List<String> dataBases = query.getResultList();
        return dataBases;
    }

    public boolean executeSQL(String sqlAll) {
        try {
            String[] sqls = sqlAll.split(";");//按照换行分割sql文件
            for (String sql : sqls) {
                if(StringUtils.isNotEmpty(sql.trim())){
                    Query query = entityManager.createNativeQuery(sql);
                    query.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

package io.znz.jsite.core.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BackupDao {

    public List<String> listTables(String catalog);

    public String getDefaultCatalog() throws SQLException;

    public List<Map<String, String>> listFields(String tablename);

    public String createTableDDL(String tablename);

    public List<Object[]> createTableData(String tablename);

    public boolean executeSQL(String sql);

    public List<String> listDataBases();

}

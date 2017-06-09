package io.znz.jsite.core.service;

import io.znz.jsite.core.dao.BackupDao;
import io.znz.jsite.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BackupService {

    private Runtime runtime = Runtime.getRuntime();
    private Process process = null;

    @Autowired
    private BackupDao backupDao;

    @Value("${mysql.home}")
    private String mysqlHome;
    @Value("${database.ip}")
    private String ip;
    @Value("${database.user}")
    private String username;
    @Value("${database.password}")
    private String password;

    @Transactional(readOnly = true)
    public String getDefaultCatalog() throws SQLException {
        return backupDao.getDefaultCatalog();
    }

    @Transactional(readOnly = true)
    public List<String> listTabels(String catalog) {
        return backupDao.listTables(catalog);
    }

    @Transactional(readOnly = true)
    public List<Map<String, String>> listFields(String tablename) {
        return backupDao.listFields(tablename);
    }


    @Transactional(readOnly = true)
    public String createTableDDL(String tablename) {
        return backupDao.createTableDDL(tablename);
    }

    @Transactional(readOnly = true)
    public List<Object[]> createTableData(String tablename) {
        return backupDao.createTableData(tablename);
    }

    public Boolean executeSQL(String sql) {
        return backupDao.executeSQL(sql);
    }

    @Transactional(readOnly = true)
    public List<String> listDataBases() {
        return backupDao.listDataBases();
    }


    private static String line = "";

    public String getCurrProgress() {
        return line == null ? "" : line;
    }

    private void executeCommand(final String command) {
        new Thread() {
            public void run() {
                line = "";
                boolean isSuccess = true;
                try {
                    //TODO:兼容更多的操作系统
                    String os = System.getProperty("os.name").toLowerCase(); //操作系统名称
                    String[] commands = null;
                    if (os.startsWith("windows")) {
                        commands = new String[]{"cmd", "/C", command};
                    } else if (os.startsWith("linux") || os.startsWith("mac")) {
                        commands = new String[]{"/bin/bash", "-c", command};
                    }
                    process = runtime.exec(commands);
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    //读取ErrorStream很关键，这个解决了挂起的问题。
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    if (process.waitFor() != 0) {
                        isSuccess = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isSuccess = false;
                }
                if (isSuccess) {
                    line = "success";
                }
            }
        }.start();
    }

    /**
     * 获取备份数据库的执行语句，备份文件名称，以当前时间命名
     * @param backPath 文件保存的根目录
     * @param tables 需要备份的表
     * @return 合成的导出命令
     */
    private String getBackupCommand(String backPath, String[] tables) {
        StringBuffer sbu = new StringBuffer();
        try {
            String database = getDefaultCatalog();
            sbu.append(mysqlHome);
            sbu.append("mysqldump ");
            sbu.append("-h ").append(ip).append(" ");
            sbu.append("-u").append(username).append(" ");
            sbu.append("-p").append(password).append(" ");
            /*
             * 这只是一个快捷选项，等同于同时添加
             * --add-drop-table,--add-locks,--create-options,--quick,--extended-insert,
             * --lock-tables,--set-charset,--disable-keys选项。
             * 本选项能让 mysqldump 很快的导出数据，并且导出的数据能很快导回。
             * 该选项默认开启，但可以用 --skip-opt 禁用。
             */
            sbu.append("--opt ");
            //该选项在导出大表时很有用，它强制 mysqldump 从服务器查询取得记录直接输出而不是取得所有记录后将它们缓存到内存中
            sbu.append("-q ");
            //sbu.append("--lock-all-tables=true ");
            //sbu.append("--complete-insert=false ");
            //每条记录对应一个insert语句
            sbu.append("--extended-insert=false ");
            sbu.append("--result-file=");
            //-start-数据库备份文件名称----------------------
            String backFilePathName = backPath + File.separator + database.toUpperCase() + "-" + DateUtils.getDateRandom() + ".sql";
            sbu.append(backFilePathName);
            //-end--数据库备份文件名称----------------------
            sbu.append(" ");
            sbu.append("--default-character-set=utf8 ");
            sbu.append(database);
            //如果有选中的表则仅备份选中的数据表
            if (tables != null && tables.length > 0) {
                for (String table : tables) {
                    sbu.append(" ").append(table);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sbu.toString();
    }

    public void doBackup(String backPath, String[] tables) {
        final String command = getBackupCommand(backPath, tables);
        executeCommand(command);
    }

    public void doRevert(final String backPath, final String dbName) {
        StringBuffer sbu = new StringBuffer();
        sbu.append(mysqlHome);
        sbu.append("mysql ");
        sbu.append("-u").append(username).append(" ");
        sbu.append("-p").append(password).append(" ");
        sbu.append("-h ").append(ip).append(" ");
        sbu.append(dbName).append(" < ").append(backPath);
        executeCommand(sbu.toString());
//        new Thread() {
//            public void run() {
//                line = "";
//                boolean isSuccess = true;
//                try {
//                    StringBuffer sbu = new StringBuffer();
//                    sbu.append(mysqlHome);
//                    sbu.append("mysqldump ");
//                    sbu.append("-h ").append(ip).append(" ");
//                    sbu.append("-u").append(username).append(" ");
//                    sbu.append("-p").append(password).append(" ");
//                    sbu.append(dbName).append(" < ").append(backPath);
//
//
//                    // 调用 mysql 的 cmd:
//                    process = runtime.exec(sbu.toString());
//
//                    String mysql = "mysql "+"-h"+serverUrl+" -u" + username + " -p"+ password + " " + dbName + " < " + restoreFile;
//
//                    System.out.println(mysql);
//
//                    java.lang.Runtime.getRuntime().exec("cmd /c " + mysql);
//
//
//                    OutputStream out = process.getOutputStream();//控制台的输入信息作为输出流
//                    StringBuffer sb = new StringBuffer("");
//                    //下面的InputStreamReader和OutputStreamWriter的第二个参数为数据的编码格式，
//                    // 注意要跟备份的格式一样，否则会有异常：java.io.IOException: 管道已结束。
//                    line = "";
//                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(backPath), "utf-8"));
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line + "/r/n");
//                    }
//                    OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
//                    writer.write(sb.toString());
//                    writer.flush();
//                    // 别忘记关闭输入输出流
//                    out.close();
//                    br.close();
//                    writer.close();
//                    if (process.waitFor() != 0) {
//                        isSuccess = false;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    isSuccess = false;
//                }
//                if (isSuccess) {
//                    line = "success";
//                }
//            }
//        }.start();
    }

}

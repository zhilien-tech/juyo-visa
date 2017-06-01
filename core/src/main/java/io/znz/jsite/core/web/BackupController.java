package io.znz.jsite.core.web;

import io.znz.jsite.core.service.BackupService;
import io.znz.jsite.core.service.ResourceService;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.ext.upload.RealPathAware;
import io.znz.jsite.util.ZipUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chaly on 15/11/3.
 * 数据库备份处理
 */
@Controller
@RequestMapping("sys/backup")
public class BackupController {

    private Logger logger = LoggerFactory.getLogger(BackupController.class);

    @Autowired
    private RealPathAware realPathAware;
    @Autowired
    private BackupService backupService;
    @Autowired
    private ResourceService resourceService;

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "sys/backupList";
    }

    /**
     * 手动备份页面
     */
    @RequestMapping(value = "mysql/tables", method = RequestMethod.GET)
    public String manual() {
        return "sys/tableList";
    }

    /**
     * 获取数据库表信息JSON
     */
    @RequiresPermissions("sys:backup:tables")
    @RequestMapping(value = "mysql/tables/json", method = RequestMethod.GET)
    @ResponseBody
    public Object listTables(Integer id) {
        List<String> tables = new ArrayList<String>();
        try {
            tables = backupService.listTabels(backupService.getDefaultCatalog());
        } catch (SQLException e) {}
        return tables;
    }

    @RequiresPermissions("sys:backup:fields")
    @RequestMapping("mysql/fields/json")
    @ResponseBody
    public Object listFiled(String tablename) {
        List<Map<String, String>> list = backupService.listFields(tablename);
        return list;
    }


    @RequiresPermissions("sys:backup:save")
    @RequestMapping(value = "mysql/backup", method = RequestMethod.POST)
    @ResponseBody
    public String backup(@RequestBody String tableNames[]) throws IOException, InterruptedException {
        String backpath = realPathAware.getRealPath(Const.BACKUP_PATH);
        File backDirectory = new File(backpath);
        if (!backDirectory.exists()) {
            backDirectory.mkdir();
        }
        backupService.doBackup(backpath, tableNames);
        return "success";
    }

    @RequiresPermissions("sys:backup:progress")
    @RequestMapping(value = "mysql/progress", method = RequestMethod.POST)
    @ResponseBody
    public Object getBackupProgress() {
        return backupService.getCurrProgress();
    }


    @RequiresPermissions("sys:backup:files")
    @RequestMapping("mysql/files/json")
    @ResponseBody
    public Object listFiles() {
        return resourceService.listFile(Const.BACKUP_PATH, false);
    }

    @RequiresPermissions("sys:backup:rename")
    @RequestMapping(value = "mysql/rename", method = RequestMethod.POST)
    @ResponseBody
    public String renameSubmit(String origName, String distName) {
        String orig = Const.BACKUP_PATH + origName;
        String dist = Const.BACKUP_PATH + distName;
        resourceService.rename(orig, dist);
        return "success";
    }

    @RequiresPermissions("sys:backup:export")
    @RequestMapping(value = "mysql/export", method = RequestMethod.POST)
    public String export(String names[], HttpServletResponse response) throws UnsupportedEncodingException {
        String backName = "back";
        if (names != null && names.length > 0 && names[0] != null) {
            backName = names[0];
        }
        List<ZipUtil.FileEntry> fileEntrys = new ArrayList<ZipUtil.FileEntry>();
        response.setContentType("application/x-download;charset=UTF-8");
        response.addHeader("Content-disposition", "filename=" + backName + ".zip");
        for (int i = 0; i < names.length; i++) {
            File file = new File(realPathAware.getRealPath(Const.BACKUP_PATH + names[i]));
            fileEntrys.add(new ZipUtil.FileEntry("", "", file));
        }
        try {
            // 模板一般都在windows下编辑，所以默认编码为GBK
            ZipUtil.zip(response.getOutputStream(), fileEntrys, "GBK");
        } catch (IOException e) {
            logger.error("export db error!", e);
        }
        return null;
    }

    @RequiresPermissions("sys:backup:delete")
    @RequestMapping(value = "mysql/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestBody String[] names) {
        resourceService.delete(names);
        return "success";
    }

    @RequiresPermissions("sys:backup:revert")
    @RequestMapping("mysql/databases")
    @ResponseBody
    public Object listDataBases() throws SQLException {
        String defaultCatalog = backupService.getDefaultCatalog();
        List<String> databases = backupService.listDataBases();
        List<Map<String, Object>> resultMaps = new ArrayList<Map<String, Object>>();
        for (String database : databases) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", database);
            map.put("text", database);
            map.put("selected", database.equals(defaultCatalog));
            resultMaps.add(map);
        }
        return resultMaps;
    }

    @RequiresPermissions("sys:backup:revert")
    @RequestMapping(value = "mysql/revert", method = RequestMethod.POST)
    @ResponseBody
    public String revert(String filename, String db) throws IOException {
        if (StringUtils.isEmpty(db)) {
            return "恢复到的数据库不能为空!";
        }
        String backFilePath = realPathAware.getRealPath(Const.BACKUP_PATH) + File.separator + filename;
        backupService.doRevert(backFilePath, db);
        return "success";
    }
}

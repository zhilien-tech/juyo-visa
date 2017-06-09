package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.PropertyFilter;
import io.znz.jsite.core.bean.Plugin;
import io.znz.jsite.core.service.PluginService;
import io.znz.jsite.core.service.ResourceService;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.ext.plugin.IPlugin;
import io.znz.jsite.ext.upload.FileStore;
import io.znz.jsite.ext.upload.RealPathAware;
import io.znz.jsite.util.SpringUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * 日志controller
 *
 * @author Chaly
 */
@Controller
@RequestMapping("sys/plugin")
public class PluginController extends BaseController {

    @Value("${tomcat.username}")
    private String tomcatUsername;
    @Value("${tomcat.password}")
    private String tomcatPassword;
    //插件配置文件前缀
    @Value("${plug.prefix}")
    private String PLUG_FILE_PREFIX;
    //插件权限配置key
    @Value("${plug.perms}")
    private String PLUG_PERMS;

    @Autowired
    private PluginService pluginService;

    @Autowired
    private RealPathAware realPathAware;

    @Autowired
    protected FileStore fileRepository;
    @Autowired
    private ResourceService resourceService;

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "sys/pluginList";
    }

    /**
     * 获取插件json
     */
    @RequiresPermissions("sys:plugin:view")
    @RequestMapping("json")
    @ResponseBody
    public Map<String, Object> list(HttpServletRequest request) {
        Pageable pageable = getPageable(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        if (filters.size() > 0) {
            pageable.first();
        }
        Page<Plugin> page = pluginService.search(pageable, filters);
        return getEasyUIData(page);
    }

    /**
     * 添加插件跳转
     */
    @RequiresPermissions("sys:plugin:add")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("plugin", new Plugin());
        model.addAttribute("action", "create");
        return "sys/pluginForm";
    }

    /**
     * 添加插件跳转
     */
    @RequiresPermissions("sys:plugin:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid Plugin plugin) throws IOException {
        //检测包含文件是否冲突
        File file = new File(realPathAware.getRealPath(plugin.getPath()));
        if (file != null) {
            boolean fileConflict = isFileConflict(file);
            plugin.setFileConflict(fileConflict);
        }
        plugin.setUploadTime(Calendar.getInstance().getTime());
        pluginService.save(plugin);
        return "success";
    }

    /**
     * 修改插件的跳转
     */
    @RequiresPermissions("sys:plugin:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("plugin", pluginService.get(id));
        model.addAttribute("action", "update");
        return "sys/pluginForm";
    }

    /**
     * 修改插件信息
     */
    @RequiresPermissions("sys:plugin:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody Plugin plugin) throws IOException {
        //检测包含文件是否冲突
        File file = new File(realPathAware.getRealPath(plugin.getPath()));
        if (file != null) {
            boolean fileConflict = isFileConflict(file);
            plugin.setFileConflict(fileConflict);
        }
        plugin.setUploadTime(Calendar.getInstance().getTime());
        pluginService.save(plugin);
        return "success";
    }

    /**
     * 删除插件
     */
    @RequiresPermissions("sys:plugin:delete")
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        Plugin plugin = pluginService.get(id);
        if (plugin != null) {//把插件压缩包也删了
            resourceService.delete(new String[]{plugin.getPath()});
        }
        pluginService.delete(id);
        return "success";
    }

    /**
     * 上传
     */
    @RequiresPermissions("sys:plugin:upload")
    @RequestMapping(value = "upload")
    @ResponseBody
    public String uploadSubmit(
        @RequestParam(value = "plugFile", required = false) MultipartFile file, ModelMap model) throws IOException {
        String origName = file.getOriginalFilename();
        String ext = FilenameUtils.getExtension(origName).toLowerCase(Locale.ENGLISH);
        //检查允许上传的后缀
        if (!"zip".equals(ext)) {
            return "文件格式错误！";
        }
        String fileUrl;
        try {
            fileUrl = fileRepository.store(Const.PLUGIN_PATH, file, true);
        } catch (Exception e) {
            return "文件上传错误！";
        }
        return fileUrl;
    }

    private boolean isFileConflict(File file) throws IOException {
        if (!file.exists())
            return false;
        ZipFile zip = new ZipFile(file, "GBK");
        ZipEntry entry;
        String name;
        String filename;
        File outFile;
        boolean fileConflict = false;
        Enumeration<ZipEntry> en = zip.getEntries();
        while (en.hasMoreElements()) {
            entry = en.nextElement();
            name = entry.getName();
            if (!entry.isDirectory()) {
                name = entry.getName();
                filename = name;
                outFile = new File(realPathAware.getRealPath(filename));
                if (outFile.exists()) {
                    fileConflict = true;
                    break;
                }
            }
        }
        zip.close();
        return fileConflict;
    }

    private boolean fileExist(String filePath) {
        File file = new File(realPathAware.getRealPath(filePath));
        return file.exists();
    }

    private String getPlugPerms(File file) throws IOException {
        ZipFile zip = new ZipFile(file, "GBK");
        ZipEntry entry;
        String name, filename;
        File propertyFile;
        String plugPerms = "";
        Enumeration<ZipEntry> en = zip.getEntries();
        while (en.hasMoreElements()) {
            entry = en.nextElement();
            name = entry.getName();
            if (!entry.isDirectory()) {
                name = entry.getName();
                filename = name;
                //读取属性文件的plug.mark属性
                if (filename.startsWith(PLUG_FILE_PREFIX) && filename.endsWith(".properties")) {
                    propertyFile = new File(realPathAware.getRealPath(filename));
                    Properties p = new Properties();
                    p.load(new FileInputStream(propertyFile));
                    plugPerms = p.getProperty(PLUG_PERMS);
                }
            }
        }
        zip.close();
        return plugPerms;
    }

    /**
     * 安装
     */
    @RequiresPermissions("sys:plugin:install")
    @RequestMapping(value = "install/{id}")
    @ResponseBody
    public String install(@PathVariable("id") Integer id) throws IOException {
        //解压zip
        Plugin plug = pluginService.get(id);
        if (plug != null && fileExist(plug.getPath())) {
            File zipFile = new File(realPathAware.getRealPath(plug.getPath()));
            //有冲突不解压
            boolean fileConflict = isFileConflict(zipFile);
            if (fileConflict) {
                return "文件冲突!";
            } else {
                resourceService.unZipFile(zipFile);
                plug.setInstallTime(Calendar.getInstance().getTime());
                plug.setUsed(true);
                String plugPerms = getPlugPerms(zipFile);
                plug.setPlugPerms(plugPerms);
                pluginService.update(plug);
            }
            return "success";
        } else {
            return "插件不存在!";
        }
    }

    /**
     * 卸载
     */
    @RequiresPermissions("sys:plugin:uninstall")
    @RequestMapping("uninstall/{id}")
    @ResponseBody
    public String uninstall(@PathVariable("id") Integer id) throws IOException {
        Plugin plug = pluginService.get(id);
        if (plug != null && fileExist(plug.getPath())) {
            IPlugin iPlugin = SpringUtil.getBean(plug.getRoot(), IPlugin.class);
            if (iPlugin != null && !iPlugin.clean()) {
                return "插件" + plug.getName() + "清理失败!";
            }
            File file = new File(realPathAware.getRealPath(plug.getPath()));
            boolean fileConflict = plug.isFileConflict();
            if (!fileConflict) {
                resourceService.deleteZipFile(file);
                plug.setUninstallTime(Calendar.getInstance().getTime());
                plug.setUsed(false);
                pluginService.update(plug);
                return "success";
            } else {
                return "文件冲突!";
            }
        } else {
            return "插件不存在!";
        }
    }

    /**
     * 使用tomcat的管理控制台重新载入APP
     */
    @RequiresPermissions("sys:plugin:reload")
    @RequestMapping("reload")
    @ResponseBody
    public String reload(HttpServletRequest request) {
        String path = request.getContextPath();
        int port = request.getLocalPort();
        String host = request.getLocalName();
        final String url = "http://" + tomcatUsername + ":" + tomcatPassword + "@" + host + ":" + port + "/manager/text/reload?path=" + path;
        new Thread(new Runnable() {
            public void run() {
                try {
                    new DefaultHttpClient().execute(new HttpGet(url));
                } catch (IOException e) {
                    //TODO:把错误直接吞掉
                }
            }
        }).start();
        return "服务器正在重新加载请稍候,如失败请手动重启!";
    }

    @RequestMapping("init/{id}")
    @ResponseBody
    public String init(@PathVariable("id") Integer id) {
        Plugin plug = pluginService.get(id);
        IPlugin iPlugin = SpringUtil.getBean(plug.getRoot(), IPlugin.class);
        if (iPlugin != null && !iPlugin.init()) {
            return "插件" + plug.getName() + "初始化失败!";
        }
        return "success";
    }

    @RequestMapping("config/{id}")
    @ResponseBody
    public String config(@PathVariable("id") Integer id) {
        Plugin plug = pluginService.get(id);
        IPlugin iPlugin = SpringUtil.getBean(plug.getRoot(), IPlugin.class);
        return iPlugin.config();
    }

    @RequestMapping("more/{id}")
    @ResponseBody
    public Object more(@PathVariable("id") Integer id) {
        Plugin plug = pluginService.get(id);
        IPlugin iPlugin = SpringUtil.getBean(plug.getRoot(), IPlugin.class);
        return iPlugin.more();
    }
}

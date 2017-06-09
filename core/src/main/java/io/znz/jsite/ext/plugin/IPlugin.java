package io.znz.jsite.ext.plugin;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chaly on 15/11/16.
 */
public interface IPlugin {

    /**
     * 插件的初始化方法,在这里可以用于执行初始化SQL的执行
     */
    @Transactional(readOnly = true)
    public boolean init();

    /**
     * 返回插件配置Form页面的URL路劲
     */
    public String config();

    /**
     * 返回更多页面的菜单列表
     */
    public List<PluginMenu> more();

    /**
     * 插件卸载前清除插件产生的文件和SQL
     */
    @Transactional(readOnly = true)
    public boolean clean();

    public static class PluginMenu {
        private String icon;
        private String label;
        private String url;

        public PluginMenu() {}

        public PluginMenu(String label, String url) {
            this.label = label;
            this.url = url;
        }

        public PluginMenu(String icon, String label, String url) {
            this.icon = icon;
            this.label = label;
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

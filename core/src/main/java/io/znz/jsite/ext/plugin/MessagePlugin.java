package io.znz.jsite.ext.plugin;

/**
 * Created by Chaly on 15/11/13.
 */
public interface MessagePlugin extends IPlugin {

    /**
     * @param targets :接收者多接收者直接使用逗号分割
     * @param content :需要发送的内容
     * @param args :其他参数
     */

    public String send(String targets, String content, Object... args);
}

package io.znz.jsite.core.bean.helper;

/**
 * Created by Chaly on 16/4/20.
 */
public class PathMapper {
    private String key;
    private String path;
    private int order;

    public PathMapper() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

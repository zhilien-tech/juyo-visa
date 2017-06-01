package io.znz.jsite.visa.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Chaly on 2017/3/29.
 */
@Embeddable
public class HelpPK implements Serializable {
    @Column(name = "customer_id", columnDefinition = "bigint(20) COMMENT '关联的客户ID'")
    private long cid;
    @Column(name = "h_key", columnDefinition = "varchar(512) COMMENT '复合索引关键字'")
    private String key;

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
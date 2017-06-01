package io.znz.jsite.visa.bean;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Chaly on 2017/3/6.
 */
@Entity
@Table(name = "visa_photo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Photo {
    @EmbeddedId
    private PhotoPK id;
    @Column(name = "field_value")
    private String value;

    public PhotoPK getId() {
        return id;
    }

    public void setId(PhotoPK id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

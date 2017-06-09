package io.znz.jsite.visa.bean;

import io.znz.jsite.visa.bean.helper.Range;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Chaly on 2017/3/9.
 */
@Entity
@Table(name = "visa_photo_option", uniqueConstraints = {@UniqueConstraint(columnNames = {"field_key"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class PhotoOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "field_key")
    private String key;
    @Column(name = "field_label")
    private String label;
    private int size;
    private int height;
    private int width;
    private String ext;
    @Type(type = "yes_no")
    private boolean required;
    @Column(name = "use_for")
    @Enumerated(value = EnumType.STRING)
    private Range useFor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Range getUseFor() {
        return useFor;
    }

    public void setUseFor(Range useFor) {
        this.useFor = useFor;
    }
}

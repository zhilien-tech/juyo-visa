package io.znz.jsite.visa.bean;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Chaly on 2017/3/14.
 */
@Embeddable
@Access(AccessType.FIELD)
public class OldName {
    @Column(name = "old_last_name", columnDefinition = "varchar(255) COMMENT '中文曾用名'")
    private String oldLastName;
    @Column(name = "old_first_name", columnDefinition = "varchar(255) COMMENT '中文曾用姓'")
    private String oldFirstName;
    @Column(name = "old_last_name_en", columnDefinition = "varchar(255) COMMENT '英文曾用名'")
    private String oldLastNameEN;
    @Column(name = "old_first_name_en", columnDefinition = "varchar(255) COMMENT '英文曾用姓'")
    private String oldFirstNameEN;

    public String getOldLastName() {
        return oldLastName;
    }

    public void setOldLastName(String oldLastName) {
        this.oldLastName = oldLastName;
    }

    public String getOldFirstName() {
        return oldFirstName;
    }

    public void setOldFirstName(String oldFirstName) {
        this.oldFirstName = oldFirstName;
    }

    public String getOldLastNameEN() {
        return oldLastNameEN;
    }

    public void setOldLastNameEN(String oldLastNameEN) {
        this.oldLastNameEN = oldLastNameEN;
    }

    public String getOldFirstNameEN() {
        return oldFirstNameEN;
    }

    public void setOldFirstNameEN(String oldFirstNameEN) {
        this.oldFirstNameEN = oldFirstNameEN;
    }
}

package io.znz.jsite.visa.bean;

import io.znz.jsite.visa.bean.helper.Relation;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by Chaly on 2017/3/6.
 */
@Entity
@Table(name = "visa_together")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Together {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "last_name", columnDefinition = "varchar(255) COMMENT '中文名'")
    private String lastName;
    @Column(name = "first_name", columnDefinition = "varchar(255) COMMENT '中文姓'")
    private String firstName;
    @Column(name = "last_name_en", columnDefinition = "varchar(255) COMMENT '英文名'")
    private String lastNameEN;
    @Column(name = "first_name_en", columnDefinition = "varchar(255) COMMENT '英文姓'")
    private String firstNameEN;

    @Enumerated(EnumType.STRING)
    private Relation relation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastNameEN() {
        return lastNameEN;
    }

    public void setLastNameEN(String lastNameEN) {
        this.lastNameEN = lastNameEN;
    }

    public String getFirstNameEN() {
        return firstNameEN;
    }

    public void setFirstNameEN(String firstNameEN) {
        this.firstNameEN = firstNameEN;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }
}

package io.znz.jsite.core.bean;

import io.znz.jsite.util.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "sys_message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Message {
    //技能ID
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    //发送人
    @JoinColumn(name = "FROM_USER_ID")
    @OneToOne
    private User from;

    //类型字典 KEY:MESSAGE,VALUE:0:系统通知;1:留言消息;2:到岗确认;
    @JoinColumn(name = "DICT_ID")
    @OneToOne
    private Dict type;

    //标题
    @Size(min = 1, max = 30)
    @NotNull
    @Column(name = "TITLE")
    private String title;

    //内容
    @Basic(optional = true)
    @Lob
    @Column(name = "CONTENT")
    private String content;

    //时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate = new Date();
    //父类ID
    @JoinColumn(name = "PID")
    @ManyToOne
    private Message parent;

    @Column(name = "URL")
    private String url;

    @Column(name = "REMARK")
    private String remark;

    public String getCreateDateLabel() {
        if (DateUtils.isSameDay(new Date(), createDate)) {
            return DateFormatUtils.format(createDate, "HH:mm");
        } else {
            return DateFormatUtils.format(createDate, "MM-dd");
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Dict getType() {
        return type;
    }

    public void setType(Dict type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Message getParent() {
        return parent;
    }

    public void setParent(Message parent) {
        this.parent = parent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

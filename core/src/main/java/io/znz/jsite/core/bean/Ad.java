package io.znz.jsite.core.bean;

import io.znz.jsite.core.bean.helper.AdType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Chaly on 15/6/30.
 */

@Entity
@Table(name = "conf_ad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //标题头衔
    private String title;

    //类型,0:图片;1:文本;
    @Enumerated(EnumType.STRING)
    private AdType type;

    //广告位置
    @JoinColumn(name = "POSITION")
    @ManyToOne
    private Dict position;

    //图片连接地址
    @Column(name = "URL")
    private String url;

    //如果是文字则记录一个html的内容
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "HTML")
    private String html;

    //点击后的链接地址
    @Column(name = "LINK")
    private String link;

    //图片宽度
    @Column(name = "WIDTH")
    private int width;
    //图片高度
    @Column(name = "HEIGHT")
    private int height;

    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate = new Date();

    //开始建间
    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    //结束建间
    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    //权重值越大越靠前
    @Column(name = "WEIGHT")
    private int weight;

    //是否启用
    @Column(name = "ENABLED")
    private boolean enabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AdType getType() {
        return type;
    }

    public void setType(AdType type) {
        this.type = type;
    }

    public Dict getPosition() {
        return position;
    }

    public void setPosition(Dict position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

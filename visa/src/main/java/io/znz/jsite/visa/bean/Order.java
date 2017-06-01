package io.znz.jsite.visa.bean;

import io.znz.jsite.core.bean.User;
import io.znz.jsite.visa.bean.helper.DataFrom;
import io.znz.jsite.visa.bean.helper.Range;
import io.znz.jsite.visa.bean.helper.TripType;
import io.znz.jsite.visa.bean.helper.VisaType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created by Chaly on 2017/3/6.
 */
@Entity
@Table(name = "visa_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "io.znz.jsite.base.bean.TableIdGenerator",
        parameters = {
            @org.hibernate.annotations.Parameter(name = "table_name", value = "sys_id_generator"),
            @org.hibernate.annotations.Parameter(name = "value_column_name", value = "ID_VALUE"),
            @org.hibernate.annotations.Parameter(name = "segment_column_name", value = "ID_NAME"),
            @org.hibernate.annotations.Parameter(name = "segment_value", value = "KEY_VISA_ORDER")
        }
    )
    private Long id;
    @Column(columnDefinition = "varchar(255) COMMENT '面签时段'")
    private String period;
    //开始建间
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;
    //结束建间
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate = new Date();
    @Column(nullable = false, columnDefinition = "INT default 0")
    private int priority = 0;
    @Column(columnDefinition = "varchar(512) COMMENT '面签时间要求'")
    private String remark;
    //快递方式
    private String delivery;
    @Column(columnDefinition = "varchar(512) COMMENT '收货地址,如果是银行自取就是银行的地址'")
    private String address;
    @Column(columnDefinition = "varchar(512) COMMENT '中信银行自取省份'")
    private String city;
    @Column(name = "use_for")
    @Enumerated(value = EnumType.STRING)
    private Range useFor;
    @Formula("(SELECT COALESCE(COUNT(oc.order_id),0) FROM visa_order_customer oc WHERE oc.order_id = id)")
    private int amount;
    private String mobile;
    private String email;
    private String pwd;
    private String contact;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(cascade = CascadeType.ALL)
    @OrderBy("home ASC,main DESC")
    @JoinTable(name = "visa_order_customer", inverseJoinColumns = @JoinColumn(name = "customer_id"), joinColumns = @JoinColumn(name = "order_id"))
    private List<Customer> customers;

    private String source;//来源渠道
    @Enumerated(value = EnumType.STRING)
    @Column(name = "trip_type")
    private TripType tripType;//行程类型
    @Enumerated(value = EnumType.STRING)
    @Column(name = "data_from")
    private DataFrom dataFrom;//材料来源 快递 \前台\其它
    private String postid;//快递单号
    @Type(type = "yes_no")
    @Column(columnDefinition = "char(1) DEFAULT 'N' COMMENT '是否开发票默认不开'")
    private boolean invoice = false;//是否开发票
    @Column(name = "invoice_title")
    private String invoiceTitle;//发票抬头
    @Column(name = "invoice_content")
    private String invoiceContent;//发票内容
    @Column(name = "invoice_sum", columnDefinition = "DOUBLE DEFAULT 0")
    private double invoiceSum;//发票金额
    @Enumerated(value = EnumType.STRING)
    @Column(name = "visa_type")
    private VisaType visaType;//签证类型
    @Type(type = "yes_no")
    @Column(columnDefinition = "char(1) DEFAULT 'N' COMMENT '是否加急默认不加急'")
    private boolean urgent = false;//是否加急

    @Temporal(TemporalType.DATE)
    @Column(name = "send_date")
    private Date sendDate;//受理送签日期
    @Temporal(TemporalType.DATE)
    @Column(name = "estimate_date")
    private Date estimateDate;//预计发给日期

    String aim;//出行目的，例：旅游
    @OneToOne(cascade = CascadeType.ALL)
    private Ticket depart;//出境
    @OneToOne(cascade = CascadeType.ALL)
    private Ticket entry;//入境
    @ManyToMany(cascade = CascadeType.ALL)
    @OrderBy("date")
    @JoinTable(name = "visa_order_trip", inverseJoinColumns = @JoinColumn(name = "trip_id"), joinColumns = @JoinColumn(name = "order_id"))
    private List<Trip> trips;//行程安排
    private String template;

    public String getTemplate() {
        if (template == null) template = "hasee";
        return template;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Range getUseFor() {
        return useFor;
    }

    public void setUseFor(Range useFor) {
        this.useFor = useFor;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public DataFrom getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(DataFrom dataFrom) {
        this.dataFrom = dataFrom;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isInvoice() {
        return invoice;
    }

    public void setInvoice(boolean invoice) {
        this.invoice = invoice;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public double getInvoiceSum() {
        return invoiceSum;
    }

    public void setInvoiceSum(double invoiceSum) {
        this.invoiceSum = invoiceSum;
    }

    public VisaType getVisaType() {
        return visaType;
    }

    public void setVisaType(VisaType visaType) {
        this.visaType = visaType;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(Date estimateDate) {
        this.estimateDate = estimateDate;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }

    public Ticket getDepart() {
        return depart;
    }

    public void setDepart(Ticket depart) {
        this.depart = depart;
    }

    public Ticket getEntry() {
        return entry;
    }

    public void setEntry(Ticket entry) {
        this.entry = entry;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}

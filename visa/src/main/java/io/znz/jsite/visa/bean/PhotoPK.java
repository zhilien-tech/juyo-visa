package io.znz.jsite.visa.bean;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Chaly on 2017/3/6.
 */
@Embeddable
public class PhotoPK implements Serializable {
    @ManyToOne
    @JSONField(serialize = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "option_id")
    private PhotoOption option;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PhotoOption getOption() {
        return option;
    }

    public void setOption(PhotoOption option) {
        this.option = option;
    }
}

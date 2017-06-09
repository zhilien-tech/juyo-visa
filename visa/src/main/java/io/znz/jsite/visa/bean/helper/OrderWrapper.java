package io.znz.jsite.visa.bean.helper;

import io.znz.jsite.visa.bean.Customer;
import io.znz.jsite.visa.bean.Travel;

/**
 * Created by Chaly on 2017/3/10.
 */
public class OrderWrapper {
    private long oid;
    private Travel travel;
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }
}

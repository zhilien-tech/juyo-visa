package io.znz.jsite.visa.bean;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Chaly on 2017/4/7.
 */
@Entity
@Table(name = "visa_trip")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(TemporalType.DATE)
    private Date date;//日程安排日期
    @OneToOne(cascade = CascadeType.REMOVE)
    private Hotel hotel;
    @Temporal(TemporalType.TIME)
    @JSONField(format = "HH:mm")
    private Date startTime;
    @Temporal(TemporalType.TIME)
    @JSONField(format = "HH:mm")
    private Date endTime;
    private String room;
    private String breakfast;
    private String dinner;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "visa_trip_scenic", inverseJoinColumns = @JoinColumn(name = "scenic_id"), joinColumns = @JoinColumn(name = "trip_id"))
    private List<Scenic> scenics;//景点

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public List<Scenic> getScenics() {
        return scenics;
    }

    public void setScenics(List<Scenic> scenics) {
        this.scenics = scenics;
    }
}

package io.znz.jsite.visa.bean;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

/**
 * Created by Chaly on 2017/3/13.
 */
@Embeddable
@Access(AccessType.FIELD)
public class Option {
    private String text;
    private String value;

    public Option() {
    }

    public Option(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

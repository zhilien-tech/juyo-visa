package io.znz.jsite.base.bean;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.enhanced.TableGenerator;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chaly on 15/8/22.
 */
public class TableIdGenerator extends TableGenerator {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
    private DecimalFormat decimalFormat = new DecimalFormat("000000");

    @Override
    public synchronized Serializable generate(SessionImplementor session, Object obj) {
        Serializable id = super.generate(session, obj);
        return Long.parseLong(dateFormat.format(new Date()) + decimalFormat.format(id) + RandomStringUtils.randomNumeric(2));
    }
}

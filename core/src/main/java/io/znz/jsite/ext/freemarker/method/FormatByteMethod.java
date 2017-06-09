package io.znz.jsite.ext.freemarker.method;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Chaly on 16/6/13.
 */
@Component("formatByteMethod")
public class FormatByteMethod implements TemplateMethodModelEx {

    private DecimalFormat df = new DecimalFormat("####.00");

    private long K = 1024;
    private long M = 1024 * K;
    private long G = 1024 * M;
    private long T = 1024 * G;
    private long P = 1024 * T;

    public String formatByte(long total) {
        if (total >= P) {
            return df.format((((double) total) / P)) + "P";
        } else if (total >= T) {
            return df.format((((double) total) / T)) + "T";
        } else if (total >= G) {
            return df.format((((double) total) / G)) + "G";
        } else if (total >= M) {
            return df.format((((double) total) / M)) + "M";
        } else if (total >= K) {
            return df.format((((double) total) / K)) + "K";
        } else {
            return df.format((double) total) + "B";
        }
    }

    public Object exec(List args) throws TemplateModelException {
        if (args.size() < 1) {
            throw new TemplateModelException("Wrong arguments");
        }
        if (args.size() == 2) {
            df = new DecimalFormat(args.get(1).toString());
        }
        return formatByte(((SimpleNumber) args.get(0)).getAsNumber().longValue());
    }
}
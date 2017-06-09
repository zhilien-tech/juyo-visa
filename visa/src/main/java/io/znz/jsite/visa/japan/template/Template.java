package io.znz.jsite.visa.japan.template;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.util.DateUtils;
import io.znz.jsite.util.StringUtils;
import io.znz.jsite.visa.bean.Customer;
import io.znz.jsite.visa.bean.Flight;
import io.znz.jsite.visa.bean.History;
import io.znz.jsite.visa.bean.Option;
import io.znz.jsite.visa.bean.Order;
import io.znz.jsite.visa.bean.Spouse;
import io.znz.jsite.visa.bean.Ticket;
import io.znz.jsite.visa.bean.Trip;
import io.znz.jsite.visa.bean.Work;
import io.znz.jsite.visa.bean.helper.Gender;
import io.znz.jsite.visa.bean.helper.Relation;
import io.znz.jsite.visa.bean.helper.VisaType;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Chaly on 2017/5/2.
 */
public abstract class Template {

    private static final Map<String, String> defaultApplyMap = new HashMap<String, String>();

    protected static final SimpleDateFormat df0 = new SimpleDateFormat("dd/MM/yyyy");
    protected static final SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
    protected static final SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
    protected static final SimpleDateFormat df3 = new SimpleDateFormat("yyyy年MM月dd日");
    protected static final SimpleDateFormat df4 = new SimpleDateFormat("MM月dd日");
    protected static final SimpleDateFormat df5 = new SimpleDateFormat("MM/dd HH:mm");
    protected static final SimpleDateFormat df6 = new SimpleDateFormat("ddMMM", Locale.ENGLISH);
    protected static final SimpleDateFormat df7 = new SimpleDateFormat("HHmm");
    protected static final SimpleDateFormat df8 = new SimpleDateFormat("yyyy-MM-dd");

    protected static final String df9 = "yy.MM.dd";

    //申请表的默认值
    static {
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[10].T2[0]", "英文姓");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[10].tyuubun[0]", "中文姓");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[11].T7[0]", "英文名");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[11].tyuubun[1]", "中文名");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[12].T16[1]", "无");//英文曾用名
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[12].tyuubun[2]", "无");//中文曾用名
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[3].#area[4].T14[0]", "出生日期");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[3].#area[4].T16[0]", "出生地点");
        //申请人性别 0:男;1:女;
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[5].#area[6].#area[7].RB1[0]", "0");
        //婚姻状况 1:单身;2:已婚;3:离婚;4:丧偶;
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[8].RB2[0]", "1");
        defaultApplyMap.put("topmostSubform[0].Page1[0].T50[0]", "CHN");//国籍
        defaultApplyMap.put("topmostSubform[0].Page1[0].T34[0]", "");//其他国籍
        defaultApplyMap.put("topmostSubform[0].Page1[0].T37[0]", "身份证号码");

        //护照类型 0/2:外交;1:普通;3:公务;4:其它;
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[1].#area[2].RB3[0]", "1");
        defaultApplyMap.put("topmostSubform[0].Page1[0].T49[0]", "护照号码");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[9].T57[1]", "签发地点");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[9].T53[0]", "签发日期");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[0].T57[0]", "签发机关");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[0].T59[0]", "有效日期");

        defaultApplyMap.put("topmostSubform[0].Page1[0].T62[0]", "赴日目的");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[21].T68[2]", "预定在日逗留开始日期");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[21].T68[3]", "预定在日逗留结束日期");
        defaultApplyMap.put("topmostSubform[0].Page1[0].T66[0]", "预定在日逗留周期");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[15].#area[16].T68[0]", "入境口岸");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[15].#area[16].T68[1]", "船舶或航空公司名称");

        defaultApplyMap.put("topmostSubform[0].Page1[0].T0[1]", "家庭地址");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[17].#area[18].T97[0]", "家庭电话");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[17].#area[18].T3[0]", "家庭手机");
        defaultApplyMap.put("topmostSubform[0].Page1[0].T3[1]", "家庭电邮");

        defaultApplyMap.put("topmostSubform[0].Page1[0].T64[0]", "无");//上次赴日日期及停留时间

        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[13].#area[14].emp_name[0]", "工作单位名称");
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[13].#area[14].emp_tel[0]", "工作单位电话");
        defaultApplyMap.put("topmostSubform[0].Page1[0].emp_adr[0]", "工作单位地址");
        defaultApplyMap.put("topmostSubform[0].Page1[0].T5[0]", "目前的职位");

        defaultApplyMap.put("topmostSubform[0].Page2[0].T16[1]", "配偶职位");

        /**
         * 以下的值都是固定值不用动
         */
        defaultApplyMap.put("topmostSubform[0].Page1[0].#area[19].#area[20].emp_name[1]", "参照“赴日予定表”");
        defaultApplyMap.put("topmostSubform[0].Page2[0].#area[12].#area[13].#area[14].RB5[5]", "1");//判过刑 0:是;1:否;
        defaultApplyMap.put("topmostSubform[0].Page2[0].#area[7].RB5[0]", "1");//一年以上徒刑  0:是;1:否;
        defaultApplyMap.put("topmostSubform[0].Page2[0].#area[8].RB5[1]", "1");//驱逐出境  0:是;1:否;
        defaultApplyMap.put("topmostSubform[0].Page2[0].#area[9].RB5[2]", "1");//贩毒  0:是;1:否;
        defaultApplyMap.put("topmostSubform[0].Page2[0].#area[10].RB5[3]", "1");//卖淫  0:是;1:否;
        defaultApplyMap.put("topmostSubform[0].Page2[0].#area[11].RB5[4]", "1");//贩卖人口  0:是;1:否;
        defaultApplyMap.put("topmostSubform[0].Page2[0].#area[5].T19[0]", "同上");//邀请人
        defaultApplyMap.put("topmostSubform[0].Page2[0].T150[0]", df0.format(new Date()));//申请日期
        defaultApplyMap.put("topmostSubform[0].Page2[0].T19[1]", "参照“身元保证书”");//担保人
    }

    protected String format(Date date, String pattern) {
        DateTime dt = new DateTime(date);
        return new SimpleDateFormat(pattern).format(dt.plusYears(12).toDate());
    }

    //统一规避错误字符串
    protected String filter(String string) {
        if (string == null) {
            string = "";
        }
        if ("CHIN".equalsIgnoreCase(string)) {
            string = "CHINA";
        }
        return string.toUpperCase();
    }

    //去掉二维码的白边
    private BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    //生成申请表中的二维码
    protected Image qrcode(Customer customer) {
        try {
            StringBuilder sb = new StringBuilder("1,")
                .append(customer.getPassport()).append(",")
                .append(df2.format(customer.getExpiryDate())).append(",")
                .append(customer.getLastNameEN()).append(",")
                .append(customer.getFirstNameEN()).append(",")
                .append(customer.getGender() == Gender.FEMALE ? "F" : "M").append(",")
                .append(df2.format(customer.getBirthday())).append(",")
                .append(customer.getCountry()).append(",")
                .append(customer.getIdCardNo()).append(",\"\",,\"\"");
            String content = sb.toString().toUpperCase();
            QRCodeWriter writer = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // 容错级别
            BitMatrix m = writer.encode(content, BarcodeFormat.QR_CODE, 320, 320, hints);
            m = deleteWhite(m);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(m, "jpg", out);
            return Image.getInstance(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSiteException("二维码生成异常!");
        }
    }

    protected String getFontURL() {
        return getClass().getClassLoader().getResource("simsun.ttf").toString();
    }

    protected BaseFont getBaseFont() throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont(getFontURL(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        return bf;
    }

    protected Font getFont() throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont(getFontURL(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font f = new Font(bf, 10);
        return f;
    }

    //多个PDF合并功能
    public ByteArrayOutputStream merge(List<ByteArrayOutputStream> list) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(new PdfReader(list.get(0).toByteArray()).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, out);
            document.open();
            for (int i = 0; i < list.size(); i++) {
                PdfReader reader = new PdfReader(list.get(i).toByteArray());
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
            document.close();
            IOUtils.closeQuietly(out);
            return out;
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSiteException("PDF合并异常!");
        }
    }

    public Image getSeal() throws IOException, BadElementException {
        //添加盖章
        Image img = Image.getInstance(getClass().getClassLoader().getResource(getPrefix() + "seal.jpg"));
        img.setAlignment(Image.RIGHT);
        img.scaleToFit(400, 200);//大小
        img.setIndentationRight(30);
        img.setAlignment(Paragraph.ALIGN_RIGHT);
        return img;
    }

    public Customer getMaster(Order order) {
        for (Customer c : order.getCustomers()) {
            if (c.isMain()) {
                return c;
            }
        }
        return null;
    }

    public String getMasterName(Order order) {
        for (Customer c : order.getCustomers()) {
            if (c.isMain()) {
                return c.getLastName() + c.getFirstName();
            }
        }
        return "";
    }

    public String getExcelFileName(Order order) {
        return DateUtils.formatDate(order.getEntry().getDate(), df9) + "-" +
            DateUtils.formatDate(order.getDepart().getDate(), df9) +
            getMasterName(order) + order.getCustomers().size() + "人.xlsx";
    }

    //获取模板的前缀
    public abstract String getPrefix();

    //介绍信照会
    public abstract ByteArrayOutputStream note(Order order);

    //酒店信息
    public abstract ByteArrayOutputStream hotel(Trip trip, String guest);

    //机票信息
    public abstract ByteArrayOutputStream flight(Order order);

    //关系表
    public abstract ByteArrayOutputStream relation(Order order);

    //行程表
    public abstract ByteArrayOutputStream trip(Order order);

    //人名薄
    public abstract ByteArrayOutputStream book(Order order);

    //身元保証書
    public abstract ByteArrayOutputStream guarantee(Order order);

    //电子表格导出
    public abstract ByteArrayOutputStream excel(Order order);

    //生成申请表
    public ByteArrayOutputStream apply(Order order, Customer customer) {
        try {
            Map<String, String> map = new HashMap<String, String>(defaultApplyMap);
            map.put("topmostSubform[0].Page1[0].#area[10].T2[0]", filter(customer.getLastNameEN()));
            map.put("topmostSubform[0].Page1[0].#area[10].tyuubun[0]", filter(customer.getLastName()));
            map.put("topmostSubform[0].Page1[0].#area[11].T7[0]", filter(customer.getFirstNameEN()));
            map.put("topmostSubform[0].Page1[0].#area[11].tyuubun[1]", filter(customer.getFirstName()));
            if (customer.getOldName() != null) {
                map.put("topmostSubform[0].Page1[0].#area[12].T16[1]", filter(customer.getOldName().getOldLastNameEN() + customer.getOldName().getOldFirstNameEN()));
                map.put("topmostSubform[0].Page1[0].#area[12].tyuubun[2]", filter(customer.getOldName().getOldLastName() + customer.getOldName().getOldFirstName()));
            }
            map.put("topmostSubform[0].Page1[0].#area[3].#area[4].T14[0]", df0.format(customer.getBirthday()));

            map.put("topmostSubform[0].Page1[0].#area[3].#area[4].T16[0]", filter(customer.getBirthProvinceCN() + customer.getBirthCityCN()));
            //申请人性别 0:男;1:女;
            map.put("topmostSubform[0].Page1[0].#area[5].#area[6].#area[7].RB1[0]", customer.getGender() == Gender.MALE ? "0" : "1");
            Spouse spouse = customer.getSpouse();
            if (spouse != null) {
                int state;
                switch (spouse.getState()) {
                    case MARRIED:
                        state = 2;
                        break;
                    case DIVORCE:
                        state = 3;
                        break;
                    case WIDOWED:
                        state = 4;
                        break;
                    case SINGLE:
                    default:
                        state = 1;
                        break;
                }
                //婚姻状况 1:单身;2:已婚;3:离婚;4:丧偶;
                map.put("topmostSubform[0].Page1[0].#area[8].RB2[0]", String.valueOf(state));
                map.put("topmostSubform[0].Page2[0].T16[1]", StringUtils.isBlank(spouse.getJob())?"无":spouse.getJob());
            }
            map.put("topmostSubform[0].Page1[0].T50[0]", filter(customer.getCountry()));//国籍
            for (Option option : customer.getOtherCountry()) {
                map.put("topmostSubform[0].Page1[0].T34[0]", option.getValue());
            }
            map.put("topmostSubform[0].Page1[0].T37[0]", customer.getIdCardNo());
            String type;
            if ("外交".equals(customer.getType())) {
                type = "0";
            } else if ("公务".equals(customer.getType())) {
                type = "3";
            } else if ("其它".equals(customer.getType())) {
                type = "4";
            } else {
                type = "1";
            }
            //护照类型 0/2:外交;1:普通;3:公务;4:其它;
            map.put("topmostSubform[0].Page1[0].#area[1].#area[2].RB3[0]", String.valueOf(type));
            map.put("topmostSubform[0].Page1[0].T49[0]", filter(customer.getPassport()));
            map.put("topmostSubform[0].Page1[0].#area[9].T57[1]", filter(customer.getIssueProvince()));
            map.put("topmostSubform[0].Page1[0].#area[9].T53[0]", df0.format(customer.getIssueDate()));
            map.put("topmostSubform[0].Page1[0].#area[0].T57[0]", filter(customer.getIssueCity()));
            map.put("topmostSubform[0].Page1[0].#area[0].T59[0]", df0.format(customer.getExpiryDate()));

            //出行信息
            if (order.getVisaType() == VisaType.FIVE) {
                map.put("topmostSubform[0].Page1[0].#area[19].#area[20].emp_name[1]", "");
                map.put("topmostSubform[0].Page2[0].T19[1]", "");
                map.put("topmostSubform[0].Page2[0].#area[5].T19[0]", "");
            }
            map.put("topmostSubform[0].Page1[0].T62[0]", filter(order.getAim()));
            Ticket entry = order.getEntry();
            Ticket depart = order.getDepart();
            DateTime dtEntry = new DateTime(entry.getDate());
            if (dtEntry.isBeforeNow()) {
                throw new JSiteException("入境时间不能在当前时间之前!");
            }
            DateTime dtDepart = new DateTime(depart.getDate());
            if (dtDepart.isBefore(entry.getDate().getTime())) {
                throw new JSiteException("出境时间不能在入境时间之前!");
            }
            map.put("topmostSubform[0].Page1[0].#area[21].T68[2]", df1.format(entry.getDate()));
            map.put("topmostSubform[0].Page1[0].#area[21].T68[3]", df1.format(depart.getDate()));
            map.put("topmostSubform[0].Page1[0].T66[0]", (Days.daysBetween(dtEntry, dtDepart).getDays() + 1) + "天");

            Flight flight = entry.getFlight();
            map.put("topmostSubform[0].Page1[0].#area[15].#area[16].T68[0]", filter(flight.getToCity()));
            map.put("topmostSubform[0].Page1[0].#area[15].#area[16].T68[1]", filter(flight.getLine()));

            map.put("topmostSubform[0].Page1[0].T0[1]", filter(customer.getAddress()));
            map.put("topmostSubform[0].Page1[0].#area[17].#area[18].T97[0]", filter(customer.getPhone()));
            //如果是子女且没有手机号则添加父母的手机号
            if (StringUtils.isBlank(customer.getMobile()) && customer.getRelation() == Relation.CHILDREN) {
                Customer c = getMaster(order);
                if (c != null) {
                    map.put("topmostSubform[0].Page1[0].#area[17].#area[18].T3[0]", filter(c.getMobile()));
                }
            } else {
                map.put("topmostSubform[0].Page1[0].#area[17].#area[18].T3[0]", filter(customer.getMobile()));
            }
            map.put("topmostSubform[0].Page1[0].T3[1]", "");
            //上次赴日日期及停留时间
            Collections.reverse(customer.getHistories());
            for (History history : customer.getHistories()) {
                map.put("topmostSubform[0].Page1[0].T64[0]", filter(df1.format(history.getArrivalDate()) + " " + history.getStay() + history.getPeriod().toString()));
            }
            Work work = customer.getWork();
            if (work != null) {
                map.put("topmostSubform[0].Page1[0].#area[13].#area[14].emp_name[0]", filter(work.getName()));
                map.put("topmostSubform[0].Page1[0].#area[13].#area[14].emp_tel[0]", filter(work.getPhone()));
                map.put("topmostSubform[0].Page1[0].emp_adr[0]", filter(work.getAddress()));
                map.put("topmostSubform[0].Page1[0].T5[0]", filter(work.getJob()));
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //1 不进行密码验证
            PdfReader.unethicalreading = true;
            //2 读入pdf表单
            PdfReader reader = new PdfReader(getClass().getClassLoader().getResource("apply.pdf"));
            //3 根据表单生成一个新的pdf
            PdfStamper ps = new PdfStamper(reader, stream);
            //4 获取pdf表单
            AcroFields fields = ps.getAcroFields();
            fields.removeXfa();//必须执行否则配偶职业和曾用名field名称重复
            //5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
            fields.addSubstitutionFont(getBaseFont());
            //6添加一个二维码图片
            Image image = qrcode(customer);
            image.scaleToFit(47, 47);
            image.setAbsolutePosition(73, 691);
            PdfContentByte over = ps.getOverContent(1);
            over.addImage(image);
            //7遍历pdf表单表格，同时给表格赋值
            Iterator<Map.Entry<String, AcroFields.Item>> iterator = fields.getFields().entrySet().iterator();
            map.put("topmostSubform[0].Page1[0].T0[1]", "\n" + map.get("topmostSubform[0].Page1[0].T0[1]"));
            map.put("topmostSubform[0].Page1[0].emp_adr[0]", "\n" + map.get("topmostSubform[0].Page1[0].emp_adr[0]"));
            while (iterator.hasNext()) {
                String key = iterator.next().getKey();
                //解决日期年份会少两个数字的问题
                if ("topmostSubform[0].Page1[0].#area[3].#area[4].T14[0]".equalsIgnoreCase(key)
                    || "topmostSubform[0].Page1[0].#area[0].T59[0]".equalsIgnoreCase(key)
                    ) {
                    //解决生日年份会少两个数字的问题
                    PdfArray rect = fields.getFieldItem(key).getWidget(0).getAsArray(PdfName.RECT);
                    Phrase phrase = new Phrase(map.get(key), new Font(getBaseFont(), 11));
                    ColumnText.showTextAligned(
                        ps.getOverContent(1),
                        Element.ALIGN_LEFT, phrase,
                        rect.getAsNumber(0).floatValue() + 2,
                        rect.getAsNumber(1).floatValue() + 1, 0);
                } else if (map.containsKey(key)) {
                    fields.setField(key, map.get(key));
                }
            }
            ps.setFormFlattening(true); // 这句不能少
            ps.close();
            reader.close();
            IOUtils.closeQuietly(stream);
            return stream;
        } catch (Exception e) {
            if (e instanceof JSiteException) {
                throw (JSiteException) e;
            } else {
                e.printStackTrace();
                throw new JSiteException("申请表生成异常!");
            }
        }
    }

    public int diffDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) { //同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { //闰年
                    timeDistance += 366;
                } else {//不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {//不同年
            return day2 - day1;
        }
    }

}

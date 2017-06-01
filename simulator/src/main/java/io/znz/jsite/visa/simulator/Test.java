package io.znz.jsite.visa.simulator;

import com.alibaba.fastjson.JSON;
import io.znz.jsite.visa.simulator.util.HttpClientUtil;
import io.znz.jsite.visa.simulator.util.ResultObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Chaly on 2017/3/12.
 */
public class Test {

    public static String getBaseUrl() {
//        return "http://59.110.155.140:8080/";
        return "http://127.0.0.1:8080/";
    }

    public static void main(String[] args) throws IOException {
        String json = HttpClientUtil.get(getBaseUrl() + "visa/customer/fetch");
        ResultObject<Map, Object> ro = JSON.parseObject(json, ResultObject.class);
        if (ro.getCode() == ResultObject.ResultCode.SUCCESS) {
            final String oid = String.valueOf(ro.getAttributes().get("oid"));
            String image = getBaseUrl() + String.valueOf(ro.getAttributes().get("avatar"));
            System.err.println(image);
            File tmp = new File(System.getProperty("user.dir"));
            if (!tmp.exists()) tmp.mkdirs();
            //保存头像到本地
            String imageFile = tmp.getAbsolutePath() + File.separator + oid + ".jpg";
            HttpClientUtil.download(image, imageFile);
            //请求JSON并保存到本地
            File target = new File(tmp, oid + ".json");
            //替换json 中的文件为绝对路径
            ro.getData().put("ctl00_cphMain_imageFileUpload", imageFile);
            FileUtils.writeStringToFile(target, JSON.toJSONString(ro.getData()));
            System.err.println("更新JSON数据完毕:" + target.getAbsolutePath());
        } else {
            System.err.println(ro.getMsg());
        }


//        String uri = "http://localhost:8080/res/plugin/kendoui/js/kendo.web.min.js";
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpGet get = new HttpGet(uri);
//        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//        try {
//            // 设置模拟头部
//            get.setHeader("Accept-Encoding", "gzip,deflate");
//            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.57 Safari/537.36");
//            String content = httpClient.execute(get, responseHandler);
//            System.out.println(content); // 如果gzip生效，这里会打印出乱码；否则会打印出jquery.js的内容
//
//		/*
//        HttpResponse response = httpClient.execute(get);
//		long cLen = response.getEntity().getContentLength();
//		System.out.println(cLen); // 如果gzip生效，这里打印的长度值将会是 -1 或一个比原始文件大小小很多的值
//		*/
//        } catch (Exception e) {
//            // ignore ...
//        } finally {
//            httpClient.getConnectionManager().shutdown();
//        }


//        int price = 470;
//        System.err.println("贷款:" + price * 0.9 * 0.65);
//        System.err.println("信款:" + price * 0.9 * 0.1);
//        System.err.println("契税:" + price * 0.9 * 0.015);
//        System.err.println("首付:" + (price * 0.9 * 0.25 + price * 0.9 * 0.015 + price * 0.1 + price * 0.02 * 0.95));

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        try {
//            Date date = sdf.parse("2017-03-24T16:00:00.000Z");
//            System.err.println(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        SimpleDateFormat df = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss 'GMT'Z (z)", Locale.ENGLISH);
//        System.err.println(df.format(new Date()));
//        try {
//            System.err.println(df.parse("Tue Apr 11 2017 03:00:00 GMT+0800 (CST)"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.err.println(new Date());

//        System.err.println("施常亮".hashCode());
    }
}

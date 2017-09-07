package io.znz.jsite.core.web;

import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.util.HttpClientUtil;
import io.znz.jsite.util.Md5Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Chaly on 2017/3/19.
 */
@Controller
@RequestMapping("translate")
public class TranslateController {

	private final String ENCODING = "UTF-8";

	private final String BAIDU = "baidu";
	private final String GOOGLE = "google";

	private final String URL_BAIDU = "http://api.fanyi.baidu.com/api/trans/vip/translate?q={0}&from={1}&to={2}&appid={3}&salt={4}&sign={5}";
	private final String URL_GOOGLE = "http://translate.google.cn/translate_a/single?client=gtx&sl={0}&tl={1}&dt=t&q={2}";

	@Value("${translate.baidu.appid}")
	private String baiduAppId;//百度翻译 appid
	@Value("${translate.baidu.appkey}")
	private String baiduAppKey;//百度翻译 appkey

	@RequestMapping(value = "{api}")
	@ResponseBody
	public Object translate(@PathVariable("api") String api, @RequestParam(defaultValue = "auto") String from,
			@RequestParam(defaultValue = "en") String to, String q) throws UnsupportedEncodingException {
		if (BAIDU.equalsIgnoreCase(api)) {
			if ("auto".equalsIgnoreCase(from))
				from = "zh";
			String salt = String.valueOf(System.currentTimeMillis());
			String sign = Md5Util.md5(baiduAppId + q + salt + baiduAppKey);
			String url = MessageFormat.format(URL_BAIDU, URLEncoder.encode(q, ENCODING), from, to, baiduAppId, salt,
					sign);
			JSONObject jsonObject = JSON.parseObject(HttpClientUtil.get(url));
			JSONObject segments = jsonObject.getJSONArray("trans_result").getJSONObject(0);
			return ResultObject.success(segments.getString("dst"));
		} else if (GOOGLE.equalsIgnoreCase(api)) {
			String url = MessageFormat.format(URL_GOOGLE, from, to, URLEncoder.encode(q, ENCODING));
			JSONArray segments = JSON.parseArray(HttpClientUtil.get(url)).getJSONArray(0);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < segments.size(); i++) {
				sb.append(segments.getJSONArray(i).getString(0));
			}
			return ResultObject.success(sb.toString());
		}
		return ResultObject.fail("暂无可用翻译结果");
	}
}

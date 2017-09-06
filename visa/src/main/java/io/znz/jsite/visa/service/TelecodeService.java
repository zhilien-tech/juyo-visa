package io.znz.jsite.visa.service;

import io.znz.jsite.exception.JSiteException;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.stereotype.Service;

/**
 * Created by Chaly on 2017/3/28.
 */
@Service
public class TelecodeService {

	private static final Log log = Logs.get();

	private static final String URL = "https://apps.chasedream.com/chinese-commercial-code/";
	private static final String ID_STATE = "__VIEWSTATE";
	private static final String ID_VALIDATION = "__EVENTVALIDATION";

	/**
	 * 汉字电码查询
	 * __VIEWSTATE与__EVENTVALIDATION参数会变化，
	 * 因此需要先获取页面的参数，再执行查询调用
	 *
	 */
	public Map<String, String> getTelecode(String text) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			log.info("电码查询中...");
			Document fetchDoc = Jsoup.connect(URL).get();
			Element stateElem = fetchDoc.getElementById(ID_STATE);
			String state = stateElem.val();

			Element validElem = fetchDoc.getElementById(ID_VALIDATION);
			String validation = validElem.val();

			Document doc = Jsoup.connect(URL).timeout(10000).data("__VIEWSTATE", state)
					.data("__VIEWSTATEGENERATOR", "4A91A8BF").data("__EVENTVALIDATION", validation)
					.data("txtInput", text).data("btnSearch", "查询").post();
			Elements elements = doc.getElementsByTag("td");
			int size = elements.size();
			for (int i = 0; i < size; i += 2) {
				map.put(elements.get(i).html(), elements.get(i + 1).html());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JSiteException("电码查询失败!");
		}
		return map;
	}

}
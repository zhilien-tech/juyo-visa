package io.znz.jsite.visa.service;

import io.znz.jsite.exception.JSiteException;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * Created by Chaly on 2017/3/28.
 */
@Service
public class TelecodeService {

	public Map<String, String> getTelecode(String text) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Document doc = Jsoup
					.connect("http://apps.chasedream.com/chinese-commercial-code/")
					.data("__VIEWSTATE",
							"+G/K9QEX4dZxWoxB9lcWorw37rapNKW3lSdaRA6IoaeC2bNLwZobBwjIXZQ0t5lAECR4DYQROnwTXoOh0u+xQxhw0By2Ynhd2AC2gyFFJH8NHVznxrJff9kM8YgIlAIKpwcjnz6moOeXgLS16w5RMejZzumjM2xclCvOimG/pWu66nxv0MnvB3VZMRohGBlSU00GpJzfmxh/x4hS49yz8fa574fx9LBXbX8V4osEfSMUKtHLPalCgmwqzoOubDPFV9YX3puLEeRvuwN8cLuA2hAhLDCRPcs5SW77G3nwhxz0GgIj1o5Jx5r9AAg9uMre")
					.data("__VIEWSTATEGENERATOR", "4A91A8BF")
					.data("__EVENTVALIDATION",
							"Vl5QNQ5dPK/4hAvixyiUvxHVneXYjk8Is9VGKa3R2La7A58p5cM8IzhelQ/ngkI/4Fy6BjI3RGuDabBlvu77V3VfJy5+918fU3E6EV1KGIaVDBLAo9lH/rdtbazeoc+LheMEbJYDPPnObz3dPQiASQ==")
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
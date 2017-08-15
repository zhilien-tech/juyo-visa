package io.znz.jsite.visa.simulator;

import io.znz.jsite.visa.simulator.data.jp.JapanData;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

/**
 * Created by Chaly on 2017/3/12.
 */
public class Test {

	public static String getBaseUrl() {
		//        return "http://59.110.155.140:8080/";
		return "http://127.0.0.1:8080/";
	}

	public static void main(String[] args) {
		JapanData data = new JapanData();

		data.setApplicantCnt("0");
		data.setEntryDate("2017/09/01");
		data.setLeaveDate("2017/09/07");
		data.setProposerNameCN("张宝峰");
		data.setProposerNameEN("ZHANG BAOFENG");
		data.setExcelUrl("C:/Users/user/Desktop/simulator_test/photo.xlsx");

		System.out.println(Json.toJson(data, JsonFormat.full()));
	}
}

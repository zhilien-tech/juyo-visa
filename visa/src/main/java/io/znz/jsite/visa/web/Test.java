/**
 * Test.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import java.util.Calendar;
import java.util.Date;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月14日 	 
 */
public class Test {
	public static void main(String[] args) {
		//生成订单号
		Date date = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		String ordernum = "" + now.get(Calendar.YEAR) + (now.get(Calendar.MONTH) + 1) + now.get(Calendar.DAY_OF_MONTH)
				+ "000" + 56;
		String random = "";
		for (int i = 0; i < 16 - ordernum.length(); i++) {
			int a = (int) (Math.random() * 10);
			random = random + a;
		}
		ordernum += random;
		System.out.println(ordernum);
	}
}

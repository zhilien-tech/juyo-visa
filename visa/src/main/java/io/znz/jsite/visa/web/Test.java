/**
 * Test.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月14日 	 
 */
public abstract class Test {
	public Test() {

	}

	public static void main(String args[]) {
		String strInput = "abacacde";
		//			Test ts = new Test();
		//		System.out.println(ts.strFilter(strInput));
	}

	public String strFilter(String strInput) {

		//No.1
		//开始写代码， 在此写入实现代码。 
		String temp = "";
		Set<String> s = new HashSet<String>();
		for (int i = 0; i < strInput.length(); i++) {
			s.add(strInput.charAt(i) + "");
		}
		Iterator<String> it = s.iterator();
		while (it.hasNext()) {
			String str = it.next();
			temp += str;
		}

		//end_code

		return temp;
	}
}

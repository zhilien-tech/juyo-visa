/**
 * Test.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月14日 	 
 */
public abstract class Test {

	public static void main(String args[]) {
		String a = "ABFCEF";
		String b = "CF";
		String c = "结果：";
		for (int i = 0; i < a.length();) {
			if (a.length() < 1) {
				break;
			}
			for (int j = 0; j < b.length(); j++) {
				System.out.print(a.charAt(i) + "");
				System.out.println(b.charAt(j) + "");
				if ((a.charAt(i) + "").equals(b.charAt(j) + "")) {
					c += a.charAt(i) + "";
					b = b.substring(j + 1, b.length());
					break;
				}
			}
			a = a.substring(1, a.length());
		}

		System.out.println(c);

	}
}

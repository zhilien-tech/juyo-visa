/**
 * Test.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

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
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8; //盐长度

	public static void main(String[] args) {
		/*	//		byte[] salt = Digests.generateSalt(SALT_SIZE);
			byte[] salt = Encodes.decodeHex("93219f213a6a0f27");

			System.out.println(new String(salt));
			Encodes.encodeHex(salt);

			byte[] hashPassword = Digests.sha1("123456".getBytes(), salt, HASH_INTERATIONS);
			String pwd = Encodes.encodeHex(hashPassword);
			System.out.println(pwd);*/
		Date d = new Date();
		System.out.println(d.getDate());
		;
	}
}

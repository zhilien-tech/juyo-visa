/**
 * Base64Test.java
 * test
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package test.passwordtest;

import io.znz.jsite.util.security.Digests;
import io.znz.jsite.util.security.Encodes;

/**
 * 测试给密码加密
 * @author   崔建斌
 * @Date	 2017年6月12日 	 
 */
public class Base64Test {
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8; //盐长度
	private static final int INIT_PASSWORD = 123456; //初始密码

	public static void main(String[] args) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] password = Digests.generateSalt(INIT_PASSWORD);
		byte[] hashPassword = Digests.sha1(password, salt, HASH_INTERATIONS);
		System.out.println(Encodes.encodeHex(hashPassword));
	}
}

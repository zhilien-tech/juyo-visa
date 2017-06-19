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
	private static final String INIT_PASSWORD = "123456"; //初始密码  d01e28e0af7983d3123e18898f66294f9cf22da5

	/**
	 * byte[] salt = Digests.generateSalt(SALT_SIZE);
	    user.setSalt(Encodes.encodeHex(salt));
	    byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, HASH_INTERATIONS);
	    user.setPassword(Encodes.encodeHex(hashPassword));
	 */

	public static void main(String[] args) {
		/*byte[] salt = Digests.generateSalt(SALT_SIZE);
		System.out.println(Encodes.encodeHex(salt));*/
		byte[] decodeHex = Encodes.decodeHex("13835737272ca1d1");
		//byte[] salt = "13835737272ca1d1".getBytes();

		byte[] password = INIT_PASSWORD.getBytes();
		byte[] hashPassword = Digests.sha1(password, decodeHex, HASH_INTERATIONS);
		System.out.println(Encodes.encodeHex(hashPassword));
	}
}

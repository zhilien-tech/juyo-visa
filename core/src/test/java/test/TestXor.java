/**
 * TestXor.java
 * test
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package test;

import io.znz.jsite.util.XORUtil;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   朱晓川
 * @Date	 2017年8月8日 	 
 */
public class TestXor {

	public static void main(String[] args) {

		String res = "E4E2EFADBAB9BFADE5E4FCADB9BBBABCA6BBB3A6BBB3ABBABEB1BEBDB1BABE";
		String key = "我是秘钥";
		XORUtil instance = XORUtil.getInstance();
		String xor_encodedStr = instance.encode(res, key);
		System.out.println("文本加密：" + xor_encodedStr);
		System.out.println("文本解密：" + instance.decode(xor_encodedStr, key));
		String[] split = instance.decode("E4E2EFADBAB9BFADE5E4FCADB9BBBABCA6BBB3A6BBB3ABBABEB1BEBDB1BABE", key).split(
				"&");
		for (String s : split) {
			System.out.println(s);
		}
	}
}

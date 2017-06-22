/**
 * Morse.java
 * test.morsecode
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package test.morsecode;

/**
 * 测试将英文字母转换为摩尔斯密码
 * @author   崔建斌
 * @Date	 2017年6月22日 	 
 */
public class Morse {
	static public String toMorseCode(String[] codebook, String text) {
		StringBuffer buf = new StringBuffer(text.length() * 3);
		int count = 0;
		for (char c : text.toUpperCase().toCharArray()) {
			if (count++ > 0)
				buf.append(", ");
			buf.append(codebook[c - 'A']);
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		String codebook[] = { "·-", "-···", "-·-·", "-··", "·", "··-·", "--·", "····", "··", "·---", "-·-", "·-··",
				"--", "-·", "---", "·--·", "--·-", "·-·", "···", "-", "··-", "···-", "·--", "-··-", "-·--", "--··" };
		System.out.println(toMorseCode(codebook, "helloWorld"));
	}
}

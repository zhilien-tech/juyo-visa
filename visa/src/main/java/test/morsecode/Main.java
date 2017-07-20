/**
 * Main.java
 * test.morsecode
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package test.morsecode;

import io.znz.jsite.visa.service.TelecodeService;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * 摩尔斯电码测试
 * @author   崔建斌
 * @Date	 2017年6月22日 	 
 */
public class Main {
	public char getOneChar(String A) {
		char result = 0;
		if (A.equals("*-"))
			result = 'a';
		else if (A.equals("-***"))
			result = 'b';
		else if (A.equals("-*-*"))
			result = 'c';
		else if (A.equals("-**"))
			result = 'd';
		else if (A.equals("*"))
			result = 'e';
		else if (A.equals("**-*"))
			result = 'f';
		else if (A.equals("--*"))
			result = 'g';
		else if (A.equals("****"))
			result = 'h';
		else if (A.equals("**"))
			result = 'i';
		else if (A.equals("*---"))
			result = 'j';
		else if (A.equals("-*-"))
			result = 'k';
		else if (A.equals("*-**"))
			result = 'l';
		else if (A.equals("--"))
			result = 'm';
		else if (A.equals("-*"))
			result = 'n';
		else if (A.equals("---"))
			result = 'o';
		else if (A.equals("*--*"))
			result = 'p';
		else if (A.equals("--*-"))
			result = 'q';
		else if (A.equals("*-*"))
			result = 'r';
		else if (A.equals("***"))
			result = 's';
		else if (A.equals("-"))
			result = 't';
		else if (A.equals("**-"))
			result = 'u';
		else if (A.equals("***-"))
			result = 'v';
		else if (A.equals("*--"))
			result = 'w';
		else if (A.equals("-**-"))
			result = 'x';
		else if (A.equals("-*--"))
			result = 'y';
		else if (A.equals("--**"))
			result = 'z';
		return result;
	}

	public String getOneString(char[] A, int start, int end) {
		StringBuilder result = new StringBuilder("");
		for (int i = start; i <= end; i++)
			result.append(A[i]);
		return result.toString();
	}

	public void printResult(String A) {
		int len = A.length();
		if (len < 1)
			return;
		char[] arrayA = A.toCharArray();
		ArrayList<Character> list = new ArrayList<Character>();
		for (int i = 0; i < len; i++) {
			int start = i;
			int end = i;
			for (; end < len; end++) {
				if (arrayA[end] == '|')
					break;
			}
			String tempA = getOneString(arrayA, start, end - 1);
			list.add(getOneChar(tempA));
			i = end;
		}
		//输出最终结果
		for (int i = 0; i < list.size(); i++)
			System.out.print(list.get(i));
	}

	public static void main(String[] args) {
		TelecodeService ser = new TelecodeService();
		Map<String, String> telecode = ser.getTelecode("中文");
		System.out.println(telecode);

		Main test = new Main();
		System.out.println("1、请输入你所需的信息...");
		Scanner in = new Scanner(System.in);
		String A = in.nextLine();
		test.printResult(A);
		System.out.println("<<<解码完成");
	}
}

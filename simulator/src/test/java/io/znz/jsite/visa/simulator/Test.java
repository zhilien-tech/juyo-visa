package io.znz.jsite.visa.simulator;

/**
 * Created by Chaly on 2017/3/12.
 */
public class Test {
	private static final int SUCCESS = 0;

	public static String getBaseUrl() {
		//        return "http://59.110.155.140:8080/";
		return "http://127.0.0.1:8080/";
	}

	public static void main(String[] args) {
		String cmd = "python D:\\test\\python\\test.py 364";
		int execResult = executeCommand(cmd);
		//如果命令行意外退出
		if (SUCCESS != execResult) {
			System.out.println("========error==========");
		} else {
			System.out.println("========normal==========");
		}
	}

	//执行命令行
	static int executeCommand(final String command) {
		int result = 0;
		try {
			Process p = Runtime.getRuntime().exec(command);
			result = p.waitFor();
			System.out.println("执行python命令返回结果:" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}

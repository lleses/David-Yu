package net.print.pdf.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * 执行进程
 * 
 * @author David
 * @date   2017年7月28日
 */
public class RunShell {

	public static void run(String[] cmd) throws Exception {
		String result = execute(cmd);//执行shell
		checkShellIsSucces(result);//判断脚本运行是否成功.出错则抛出异常
	}

	/** 执行shell **/
	private static String execute(String[] cmd) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(cmd).redirectErrorStream(true);
		Process child = pb.start();
		InputStream in = child.getInputStream();
		child.getOutputStream().close();
		int c;
		StringBuffer sb = new StringBuffer();
		while ((c = in.read()) != -1) {
			sb.append((char) c);
		}
		in.close();
		child.getOutputStream().close();
		child.getErrorStream().close();
		return sb.toString();
	}

	/** 判断脚本运行是否成功.出错则抛出异常 **/
	private static void checkShellIsSucces(String result) throws Exception {
		boolean checkResult = result.indexOf("timeout") >= 0 || result.indexOf("FAIL to load") >= 0 || result.indexOf("Can't open") >= 0;
		if (result != null && result.length() > 0 && (checkResult)) {
			throw new Exception(result);
		}
	}
}

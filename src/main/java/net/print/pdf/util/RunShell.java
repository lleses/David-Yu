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

	public static boolean run(String[] cmd) {
		String result = "";
		try {
			result = execute(cmd);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (result != null && result.length() > 0 && (result.indexOf("timeout") >= 0 || result.indexOf("FAIL to load") >= 0 || result.indexOf("Can't open") >= 0)) {
			try {
				throw new Exception("load file failed!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
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
}

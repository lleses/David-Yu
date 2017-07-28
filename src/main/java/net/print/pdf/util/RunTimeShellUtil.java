package net.print.pdf.util;

import java.io.IOException;
import java.io.InputStream;

public class RunTimeShellUtil {

	public static String runShell(String[] cmd) throws IOException {
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

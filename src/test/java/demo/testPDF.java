package demo;

import net.print.pdf.dto.PdfParams;
import net.print.pdf.util.PDFUtils;

public class testPDF {

	/**
	 * cmd 启动命令	phantomjs C:/Users/ann/workspace/phantomjs-dome/js/demo.js
	 */
	public static void main(String[] args) {
		PDFUtils.isTest = true;
		// 启动demo
		demo2();
		System.out.println("end..");
	}

	/** 爬虫生成pdf+后台处理pdf **/
	private static void demo() {
		// 模拟测试数据
		//		String url = "http://news.baidu.com";
		String url = "file:///C:/Users/ann/workspace/pdf-utils/src/main/webapp/html/test.html";
		String outPath = "C:/Users/ann/Desktop/testPDF/001.pdf";
		PDFUtils.phantomjs(url, outPath);
		//		PDFUtils.phantomjs(url, outPath, format);
		//		PDFUtils.phantomjs(url, outPath, width, height);
	}

	/** 爬虫生成pdf+后台处理pdf **/
	private static void demo2() {
		// 模拟测试数据
		String sourcePath = "C:/Users/ann/Desktop/testPDF/001.pdf";
		String outPath = "C:/Users/ann/Desktop/testPDF/002.pdf";
		PDFUtils.zoom(sourcePath, outPath, 1, 0.5f, 200f);
	}

	/** 模拟测试数据 **/
	private static PdfParams params() {
		PdfParams params = new PdfParams();
		params.setUrl("http://192.168.1.7:8081/newSF/html/pdf/printOverviewPDF.jsp?sessionId=3FA3D42A18BFA6E0CE01DC7EEC22EB76");
		//params.setUrl("http://news.baidu.com");
		params.setOutPath("C:/Users/ann/Desktop/testPDF/001.pdf");
		params.setFileScale(0);
		params.setFileWidthScale(0.5f);
		params.setUserName("David");
		params.setDateTime("2017-06-12");
		params.setPdfFolder("C:/Users/ann/Desktop/testPDF/");
		params.setWidth(400f);
		params.setHeight(250f);
		params.setType(1);

		String[] cmd = new String[4];
		cmd[0] = "phantomjs";
		cmd[1] = "C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js";
		cmd[2] = params.getUrl();
		cmd[3] = params.getPdfFolder() + "resource.pdf";
		params.setCmd(cmd);
		return params;

		//phantomjs C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js "http://192.168.1.7:8081/newSF/html/pdf/printOverviewPDF.jsp?sessionId=23174A281C2B75F2BC980B61A47E24AC" C:/Users/ann/Desktop/testPDF/resource.pdf 
		//phantomjs C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js "file:///C:/Users/ann/workspace/test/web/test.html" C:/Users/ann/Desktop/testPDF/overview.pdf 

		//phantomjs C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js "http://news.baidu.com/" C:/Users/ann/Desktop/testPDF/baidu.pdf 
		
		//phantomjs C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js "file:///C:/Users/ann/workspace/test/web/01.html" C:/Users/ann/Desktop/testPDF/overview.pdf 
		//phantomjs C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js "file:///C:/Users/ann/workspace/pdf-utils/src/main/webapp/html/01.html" C:/Users/ann/Desktop/testPDF/overview.pdf 
	}
}

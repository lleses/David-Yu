package demo;

import net.print.pdf.dto.PdfParams;
import net.print.pdf.service.PDFService;

public class testPDF {

	/**
	 * cmd 启动命令	phantomjs C:/Users/ann/workspace/phantomjs-dome/js/demo.js
	 */
	public static void main(String[] args) {
		// 模拟测试数据
		PdfParams params = params();
		// 启动demo
		demo(params);

		System.out.println("end..");
	}

	/** 爬虫生成pdf+后台处理pdf **/
	private static void demo(PdfParams pdfParams) {
		PDFService.getInstatnce().handPdf(pdfParams);
	}

	/** 模拟测试数据 **/
	private static PdfParams params() {
		PdfParams params = new PdfParams();
		params.setUrl("http://news.baidu.com");
		params.setOutPath("C:/Users/ann/Desktop/testPDF/demo.pdf");
		params.setFileScale(1);
		params.setUserName("David");
		params.setDateTime("2017-06-12");
		params.setPdfFolder("C:/Users/ann/Desktop/testPDF/");

		String[] cmd = new String[4];
		cmd[0] = "phantomjs";
		cmd[1] = "C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js";
		cmd[2] = params.getUrl();
		cmd[3] = params.getPdfFolder() + "resource.pdf";
		params.setCmd(cmd);
		return params;
		
		//phantomjs C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js "http://192.168.1.7:8081/newSF/html/pdf/printOverviewPDF.jsp?sessionId=C5FE8D5D1E0A829B55AD72F9D0D0F5C9" C:/Users/ann/Desktop/testPDF/resource.pdf
	}
}

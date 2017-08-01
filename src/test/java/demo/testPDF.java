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
		params.setUrl("http://192.168.1.7:8081/newSF/html/pdf/printOverviewPDF.jsp?sessionId=2EFDFE12C7FFF6D3B1E56DC4D3C24810");
		//params.setUrl("http://news.baidu.com");
		params.setOutPath("C:/Users/ann/Desktop/testPDF/001.pdf");
		params.setFileScale(1);
		params.setUserName("David");
		params.setDateTime("2017-06-12");
		params.setPdfFolder("C:/Users/ann/Desktop/testPDF/");
		params.setWidth(400f);
		params.setHeight(250f);
		params.setType(1);

		String[] cmd = new String[4];
		cmd[0] = "phantomjs";
		cmd[1] = "C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/demo.js";
		cmd[2] = params.getUrl();
		cmd[3] = params.getPdfFolder() + "resource.pdf";
		params.setCmd(cmd);
		return params;

		//phantomjs C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js "http://192.168.1.7:8081/newSF/html/pdf/printOverviewPDF.jsp?sessionId=23174A281C2B75F2BC980B61A47E24AC" C:/Users/ann/Desktop/testPDF/resource.pdf 
		//phantomjs C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js "file:///C:/Users/ann/workspace/test/web/01.html" C:/Users/ann/Desktop/testPDF/overview.pdf 
		//phantomjs C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js "file:///C:/Users/ann/workspace/test/web/test.html" C:/Users/ann/Desktop/testPDF/overview.pdf 
		
		
		
		//phantomjs C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js "http://news.baidu.com/" C:/Users/ann/Desktop/testPDF/baidu.pdf 
	}
}

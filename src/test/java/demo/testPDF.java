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
		params.setOutPath("C:/Users/ann/Desktop/testPDF/001.pdf");
		params.setFileScale(1);
		params.setUserName("David");
		params.setDateTime("2017-06-12");
		params.setPdfFolder("C:/Users/ann/Desktop/testPDF/");

		String[] cmd = new String[4];
		cmd[0] = "phantomjs";
		cmd[1] = "C:/Users/ann/workspace/pdf/src/main/webapp/res/js/demo.js";
		cmd[2] = params.getUrl();
		cmd[3] = params.getPdfFolder() + "resource.pdf";
		params.setCmd(cmd);
		return params;

		//phantomjs C:/Users/ann/workspace/pdf/src/main/webapp/res/js/demo.js "http://192.168.1.7:8081/newSF/html/pdf/printCheckListPDF.jsp?checkListParams=eyJwYXJhbXMiOnsicHJvaklkIjoxMTkyNywiY3ljbGVJZCI6MywiaXRlbSI6MSwidGFza0lkIjoyNTI2Mn0sInByb2pOYW1lIjoiYXJ0d29yayBwcm9qZWN0IiwiZmlsZU5hbWUiOiIwMDEuanBnIiwic3RhdHVzIjoxfQ==&sessionId=FFE6C21613C4537710B30F85DE86B872" C:/Users/ann/Desktop/testPDF/resource.pdf 
	}
}

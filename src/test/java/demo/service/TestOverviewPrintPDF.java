package demo.service;

import net.print.pdf.dto.OverviewPrintPDF;
import net.print.pdf.util.PDFServiceUtils;
import net.print.pdf.util.PDFUtils;

/**
 * 测试Overview业务的打印功能
 * 
 * @author David
 * @date   2017年8月9日
 */
public class TestOverviewPrintPDF {

	public static void main(String[] args) {
		PDFUtils.isTest = true;//测试模式
		overviewPrintPDF();//测试overview
		System.out.println("[overviewPrintPDF]end..");
	}

	/** 测试overview **/
	private static void overviewPrintPDF() {
		OverviewPrintPDF param = getOverviewParam();//组装参数
		PDFServiceUtils.overview(param);//打印
	}

	/** 组装参数 **/
	private static OverviewPrintPDF getOverviewParam() {
		OverviewPrintPDF param = new OverviewPrintPDF();
		param.setUrl("http://news.baidu.com");
		param.setPrintType(0);
		param.setOutFolderPath("C:/Users/ann/Desktop/testPDF/");
		param.setScale(0.5f);
		param.setPrintWidth(300f);
		param.setPrintHeight(300f);
		param.setFooter("Printed by David, 2017-08-09");
		return param;
	}
}

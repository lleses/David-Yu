package demo.util;

import net.print.pdf.util.PDFUtils;

/**
 * 测试PDF工具类(缩放PDF)
 * 
 * @author David
 * @date   2017年8月9日
 */
public class TestZoomPDF {
	/** URL **/
	private static String url = "http://news.baidu.com";
	/** 输出PDF的上一级文件夹路径 **/
	private static String outFolderPath = "C:/Users/ann/Desktop/testPDF/";

	public static void main(String[] args) {
		PDFUtils.isTest = true;
		try {
			createNotPagingPDF();
			System.out.println("[创建PDF(不分页)]end.. ");

			zoomPDByAdaptive();
			System.out.println("[缩放PDF(通过自适应)]end.. ");

			zoomPDFByPercentage();
			System.out.println("[缩放PDF(根据百分比)]end.. ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 创建PDF(不分页) **/
	private static void createNotPagingPDF() throws Exception {
		String outPath = outFolderPath + "createNotPagingPDF.pdf";
		PDFUtils.createNotPagingPDF(url, outPath);
	}

	/** 缩放PDF(通过自适应) **/
	private static void zoomPDByAdaptive() throws Exception {
		String sourcePath = outFolderPath + "createNotPagingPDF.pdf";
		String outPath = outFolderPath + "zoomPDByAdaptive.pdf";
		PDFUtils.zoomPDByAdaptive(sourcePath, outPath, 300);
	}

	/** 缩放PDF(根据百分比) **/
	private static void zoomPDFByPercentage() throws Exception {
		String sourcePath = outFolderPath + "createNotPagingPDF.pdf";
		String outPath = outFolderPath + "zoomPDFByPercentage.pdf";
		PDFUtils.zoomPDFByPercentage(sourcePath, outPath, 0.5f);
	}

}

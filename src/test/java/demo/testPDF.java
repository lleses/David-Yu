package demo;

import net.print.pdf.util.PDFUtils;

public class testPDF {

	private static String url = "http://news.baidu.com";
	/** 输出PDF的上一级文件夹路径 **/
	private static String outFolderPath = "C:/Users/ann/Desktop/testPDF/";

	public static void main(String[] args) {
		//cmd 启动命令	phantomjs C:/Users/ann/workspace/phantomjs-dome/js/demo.js
		System.out.println("[创建PDF(不分页)]end.. :处理" + createNotPagingPDF());
		System.out.println("[创建PDF(根据格式分页)]end.. :处理" + createPagingPDFByFormat());
		System.out.println("[创建PDF(根据宽高分页)]end.. :处理" + createPagingPDFByWidthAndHeight());

		System.out.println("[缩放PDF(通过自适应)]end.. :处理" + zoomPDByAdaptive());
		System.out.println("[缩放PDF(根据百分比)]end.. :处理" + zoomPDFByPercentage());
	}

	/** 创建PDF(不分页) **/
	private static boolean createNotPagingPDF() {
		String outPath = outFolderPath + "createNotPagingPDF.pdf";
		PDFUtils.isTest = true;
		return PDFUtils.createNotPagingPDF(url, outPath);
	}

	/** 创建PDF(根据格式分页) **/
	private static boolean createPagingPDFByFormat() {
		String outPath = outFolderPath + "createPagingPDFByFormat.pdf";
		PDFUtils.isTest = true;
		return PDFUtils.createPagingPDFByFormat(url, outPath, "A4");
	}

	/** 创建PDF(根据宽高分页) **/
	private static boolean createPagingPDFByWidthAndHeight() {
		String outPath = outFolderPath + "createPagingPDFByWidthAndHeight.pdf";
		PDFUtils.isTest = true;
		return PDFUtils.createPagingPDFByWidthAndHeight(url, outPath, 200, 200);
	}

	/** 缩放PDF(通过自适应) **/
	private static boolean zoomPDByAdaptive() {
		String sourcePath = outFolderPath + "createNotPagingPDF.pdf";
		String outPath = outFolderPath + "zoomPDByAdaptive.pdf";
		return PDFUtils.zoomPDByAdaptive(sourcePath, outPath, 300);
	}

	/** 缩放PDF(根据百分比) **/
	private static boolean zoomPDFByPercentage() {
		String sourcePath = outFolderPath + "createNotPagingPDF.pdf";
		String outPath = outFolderPath + "zoomPDFByPercentage.pdf";
		return PDFUtils.zoomPDFByPercentage(sourcePath, outPath, 0.5f);
	}

}

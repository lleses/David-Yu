package demo.util;

import net.print.pdf.util.PDFUtils;

/**
 * 测试PDF工具类(根据URL生成PDF)
 * 
 * @author David
 * @date   2017年8月9日
 */
public class TestCreatePDF {

	private static String url = "http://news.baidu.com";
	private static String outFolderPath = "C:/Users/ann/Desktop/testPDF/";

	public static void main(String[] args) {
		try {
			PDFUtils.isTest = true;

			createNotPagingPDF();
			System.out.println("[创建PDF(不分页)]end.. ");

			createPagingPDFByFormat();
			System.out.println("[创建PDF(根据格式分页)]end.. :处理");

			createPagingPDFByWidthAndHeight();
			System.out.println("[创建PDF(根据宽高分页)]end.. :处理");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 创建PDF(不分页) **/
	private static void createNotPagingPDF() throws Exception {
		String outPath = outFolderPath + "createNotPagingPDF.pdf";
		PDFUtils.createNotPagingPDF(url, outPath);
	}

	/** 创建PDF(根据格式分页) **/
	private static void createPagingPDFByFormat() throws Exception {
		String outPath = outFolderPath + "createPagingPDFByFormat.pdf";
		PDFUtils.createPagingPDFByFormat(url, outPath, "A4");
	}

	/** 创建PDF(根据宽高分页) **/
	private static void createPagingPDFByWidthAndHeight() throws Exception {
		String outPath = outFolderPath + "createPagingPDFByWidthAndHeight.pdf";
		PDFUtils.createPagingPDFByWidthAndHeight(url, outPath, 200, 200);
	}

}

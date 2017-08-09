package demo.util;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import net.print.pdf.util.PDFUtils;

/**
 * 测试PDF工具类(分页PDF)
 * 
 * @author David
 * @date   2017年8月9日
 */
public class TestPagingPDF {

	private static String url = "http://news.baidu.com";
	private static String outFolderPath = "C:/Users/ann/Desktop/testPDF/";
	private static String sourcePath = outFolderPath + "createNotPagingPDF.pdf";

	public static void main(String[] args) {
		try {
			PDFUtils.isTest = true;

			createNotPagingPDF();
			System.out.println("[创建PDF(不分页)]end.. ");

			pagingPDFByAdaptive();
			System.out.println("[分页PDF(根据百分比)]end.. ");

			pagingPDFByPercentage();
			System.out.println("[分页PDF(根据百分百)]end.. ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 创建PDF(不分页) **/
	private static void createNotPagingPDF() throws Exception {
		String outPath = outFolderPath + "createNotPagingPDF.pdf";
		PDFUtils.createNotPagingPDF(url, outPath);
	}

	/** 分页PDF(根据百分百) **/
	private static void pagingPDFByPercentage() throws IOException, DocumentException {
		String outPath = outFolderPath + "pagingPDF.pdf";
		PDFUtils.pagingPDFByPercentage(sourcePath, outPath, 300, 400);
	}

	/** 分页PDF(根据自适应) **/
	private static void pagingPDFByAdaptive() throws IOException, DocumentException {
		String outPath = outFolderPath + "pagingPDF.pdf";
		PDFUtils.pagingPDFByAdaptive(sourcePath, outPath, 300, 400);
	}

}

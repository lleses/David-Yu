package net.print.pdf.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import net.print.pdf.dto.PDFHeadFoot;
import net.print.pdf.dto.PdfParams;
import net.print.pdf.util.RunTimeShellUtil;

/**
 * PDF工具类
 * 
 * @author David
 * @date   2017年6月15日
 */
public class PDFService {

	private final int MARGIN_X = 36;
	private final int MARGIN_Y = 36;

	private static PDFService instance = null;

	public synchronized static PDFService getInstatnce() {
		if (instance == null) {
			instance = new PDFService();
		}
		return instance;
	}

	/**
	 * 处理pdf
	 */
	public void handPdf(PdfParams params) {
		String fileName = "resource.pdf";

		try {
			createFolder(params.getPdfFolder());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// --------------------------爬虫生成pdf--------------------------
		if (!phantomPDF(params.getCmd())) {
			return;
		}

		// --------------------------pdf比率缩放--------------------------
		if (params.getFileWidthScale() != null && params.getFileScale() == 0) {
			if (!RatioPDF(params)) {
				return;
			} else {
				fileName = "ratio.pdf";
			}
		}

		// --------------------------分页pdf--------------------------
		if (params.getWidth() != null && params.getHeight() != null && params.getFileScale() == 0) {
			if (!pagingPDF(params, fileName)) {
				return;
			} else {
				fileName = "paging.pdf";
			}
		}

		// --------------------------pdf页头、页尾--------------------------
		if (!convertPDF(params, fileName)) {
			return;
		}
	}

	private boolean phantomPDF(String[] cmd) {
		String result = "";
		try {
			result = RunTimeShellUtil.runShell(cmd);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (result != null && result.length() > 0 && (result.indexOf("timeout") >= 0 || result.indexOf("FAIL to load") >= 0)) {
			try {
				throw new Exception("load file failed!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		return true;
	}

	/** pdf比率缩放 **/
	private boolean RatioPDF(PdfParams params) {
		PdfReader reader = null;
		try {
			reader = new PdfReader(params.getPdfFolder() + "resource.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (reader == null) {
			return false;
		}
		Rectangle pagesize = reader.getPageSize(1);
		float width = pagesize.getWidth();
		float heigth = pagesize.getHeight();
		float pageWidth = 0;
		float pageHeight = 0;
		float zoom = 0;
		if (params.getFileScale() == 0) {// Scale 0%---100%
			zoom = params.getFileWidthScale();
			pageWidth = (int) (width * zoom);
			pageHeight = (int) (heigth * zoom);
		} else {// fit to width
			pageWidth = params.getWidth() - MARGIN_X * 2;
			zoom = pageWidth / width;
			pageHeight = (int) (heigth * zoom);
		}
		Document document = new Document(new Rectangle(pageWidth, pageHeight));
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(params.getPdfFolder() + "ratio.pdf"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if (writer == null) {
			return false;
		}

		// step 3
		document.open();

		// step 4
		PdfContentByte content = writer.getDirectContent();
		PdfImportedPage page = writer.getImportedPage(reader, 1);

		/**在原页面中按纸张大小,按比率缩放,不旋转**/
		content.addTemplate(page, zoom, 0, 0, zoom, 0, 0);

		// step 5
		document.close();
		reader.close();
		return true;
	}

	/** pdf缩放、裁剪、分页  **/
	private boolean pagingPDF(PdfParams params, String fileName) {
		PdfReader reader = null;// 获取pdf文件获取实例
		try {
			reader = new PdfReader(params.getPdfFolder() + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (reader == null) {
			return false;
		}

		// --------------------------step 1--------------------------

		Rectangle pagesize = reader.getPageSizeWithRotation(1);
		float width = pagesize.getWidth();// 爬虫生成的pdf宽度
		float height = pagesize.getHeight();// 爬虫生成的pdf高度

		float pageWidth = params.getWidth() - MARGIN_X * 2;// 减去边缘后的pdf宽度
		float pageHeight = params.getHeight() - MARGIN_Y * 2;// 减去边缘后的pdf高度
		int widthTotal = 1;// 宽页数
		int heightTotal = 1;// 高页数

		if (params.getFileScale() == 0) {// 左右分页
			widthTotal = (int) (width / pageWidth);
			if (width % pageWidth > 0) {
				widthTotal++;
			}
			heightTotal = (int) (height / pageHeight);
			if (height % pageHeight > 0) {
				heightTotal++;
			}
		} else {// 按宽等比率缩放
			heightTotal = (int) (height / pageHeight);
			if (height % pageHeight > 0) {
				heightTotal++;
			}

		}
		// 总页数
		int pageTotal = widthTotal * heightTotal;

		// --------------------------step 2--------------------------

		Rectangle re = new Rectangle(pageWidth, pageHeight);// 矩形实例
		Document document = new Document(re);
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(params.getPdfFolder() + "paging.pdf"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		if (writer == null) {
			return false;
		}

		// --------------------------step 3--------------------------
		document.open();

		// --------------------------step 4--------------------------
		PdfContentByte content = writer.getDirectContent();
		PdfImportedPage page = writer.getImportedPage(reader, 1);

		float x, y;
		for (int i = 0; i < pageTotal; i++) {
			// 从左边开始,按纸张的宽度读取内容
			x = -pageWidth * (i % widthTotal);
			// 从头部开始,按纸张的高度读取内容
			y = -(height - pageHeight * ((i / widthTotal) + 1));
			// 在原页面中按纸张大小,不缩放,不旋转
			content.addTemplate(page, x, y);
			document.newPage();
		}

		// --------------------------step 5--------------------------
		document.close();
		reader.close();
		return true;
	}

	/** 创建最终的ＰＤＦ文件 **/
	private boolean convertPDF(PdfParams params, String fileName) {
		// 读取原PDF文件,取出页面总数
		PdfReader reader = null;
		try {
			reader = new PdfReader(params.getPdfFolder() + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (reader == null) {
			return false;
		}

		int n = reader.getNumberOfPages();

		// 初始化PDF Document
		Float pageWidth = params.getWidth();
		Float pageHeight = params.getHeight();
		Rectangle rectangle = reader.getPageSizeWithRotation(1);
		if ((pageWidth == null || pageHeight == null) && rectangle != null) {
			pageWidth = rectangle.getWidth() + 2 * MARGIN_X;
			pageHeight = rectangle.getHeight() + 2 * MARGIN_Y;
		}

		Rectangle re = new Rectangle(pageWidth, pageHeight);
		Document document = new Document(re, MARGIN_X, MARGIN_Y, MARGIN_X, MARGIN_Y);
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(params.getOutPath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if (writer == null) {
			return false;
		}

		// 添加页脚
		PDFHeadFoot header = new PDFHeadFoot();
		header.setWidth(pageWidth);
		header.setHeader("Printed by " + params.getUserName() + ", " + params.getDateTime());
		writer.setPageEvent((PdfPageEvent) header);
		document.open();

		PdfContentByte content = writer.getDirectContent();
		PdfImportedPage page = null;
		for (int i = 1; i <= n; i++) {
			page = writer.getImportedPage(reader, i);
			// 将原文件中的内容放入新的文件中,并确定好绝对定位
			content.addTemplate(page, MARGIN_X, MARGIN_Y);
			document.newPage();
		}

		// step 4
		document.close();
		reader.close();
		return true;
	}

	private void createFolder(String folder) throws Exception {
		File file = new File(folder);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}

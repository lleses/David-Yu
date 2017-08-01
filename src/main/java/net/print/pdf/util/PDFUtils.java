package net.print.pdf.util;

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

/**
 * PDF工具类
 * 
 * @author David
 * @date   2017年6月15日
 */
public class PDFUtils {

	private final int MARGIN_X = 36;
	private final int MARGIN_Y = 36;

	public static boolean isTest = false;//是否测试模式,默认为false

	/**
	 * 爬虫生成PDF
	 * 
	 * @param url
	 * 			url路径
	 * @param outPath
	 * 			输出路径
	 * @return
	 */
	public static boolean phantomjs(String url, String outPath) {
		return phantomjs(url, outPath, null, null, null);
	}

	/**
	 * 爬虫生成PDF
	 * 
	 * @param url
	 * 			url路径
	 * @param outPath
	 * 			输出路径
	 * @param format
	 * 			打印格式:'A3', 'A4', 'A5', 'Legal', 'Letter', 'Tabloid'.
	 * @return
	 */
	public static boolean phantomjs(String url, String outPath, String format) {
		//检查打印格式是否正确
		if (checkFormat(format)) {
			return phantomjs(url, outPath, null, null, format);
		}
		return false;
	}

	/** 检查打印格式是否正确 **/
	private static boolean checkFormat(String format) {
		String[] arr = { "A3", "A4", "A5", "Legal", "Letter", "Tabloid" };
		for (String type : arr) {
			if (type.equals(format)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 爬虫生成PDF
	 * 
	 * @param url
	 * 			url路径
	 * @param outPath
	 * 			输出路径
	 * @param width
	 * 			每页宽度
	 * @param height
	 * 			每页高度
	 * @return
	 */
	public static boolean phantomjs(String url, String outPath, int width, int height) {
		return phantomjs(url, outPath, width, height, null);
	}

	/**
	 * 爬虫生成PDF
	 * 
	 * @param url
	 * 			url路径
	 * @param outPath
	 * 			输出路径
	 * @param width
	 * 			每页宽度
	 * @param height
	 * 			每页高度
	 * @param format
	 * 			打印格式:'A3', 'A4', 'A5', 'Legal', 'Letter', 'Tabloid'.
	 * @return
	 */
	private static boolean phantomjs(String url, String outPath, Integer width, Integer height, String format) {
		if (url == null || outPath == null) {
			return false;
		}
		String cmd4 = null;
		if (width != null && height != null) {
			cmd4 = width + "*" + height;
		}
		if (format != null) {
			cmd4 = format;
		}

		String path = "C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/phantomPDF.js";
		if (isTest) {
			path = "C:/Users/ann/workspace/pdf-utils/src/main/webapp/res/js/demo.js";
		}

		//组装进程命令
		String[] cmd = new String[cmd4 == null ? 4 : 5];

		cmd[0] = "phantomjs";
		cmd[1] = path;
		cmd[2] = url;
		cmd[3] = outPath;
		if (cmd4 != null) {
			cmd[4] = cmd4;
		}
		return RunShell.run(cmd);
	}

	/**
	 * 缩放PDF
	 * 
	 * @param sourcePath
	 * 				待处理文件的路径
	 * @param outPath
	 * 				输出文件的路径
	 * @param zoomType
	 * 				缩放类型:(0:百分百缩放;其他:自适应缩放)
	 * @return
	 */
	public static boolean zoom(String sourcePath, String outPath, int zoomType, float scale, float width) {
		//检查文件是否存在		

		PdfReader reader = null;
		try {
			reader = new PdfReader(sourcePath);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (reader == null) {
			return false;
		}
		Rectangle pagesize = reader.getPageSize(1);
		float w = pagesize.getWidth();
		float h = pagesize.getHeight();
		float pageWidth = 0;
		float pageHeight = 0;
		float zoom = 0;
		if (zoomType == 0) {// Scale 0%---100%
			zoom = scale;
			pageWidth = (int) (w * zoom);
			pageHeight = (int) (h * zoom);
		} else {// fit to width
			//TODO
//			pageWidth = params.getWidth() - MARGIN_X * 2;
			zoom = pageWidth / w;
			pageHeight = (int) (h * zoom);
		}
		Document document = new Document(new Rectangle(pageWidth, pageHeight));
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
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
	
	
	/**
	 * pdf添加页头，页尾等信息
	 * 
	 * @param outputFilePath
	 * 				输出路径
	 * @param sourceFilePath
	 * 				源文件路径
	 */
	public void headFootPdf(String outputFilePath, String sourceFilePath, String header, String leftBottomContent) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(sourceFilePath);
		int margin_x = 36;
		int margin_y = 36;
		int num = reader.getNumberOfPages();
		Rectangle pag = reader.getPageSizeWithRotation(1);
		float width = pag.getWidth();
		if (width < 240) {
			width = 240;
		}
		Rectangle rec = new Rectangle(width + 2 * margin_x, pag.getHeight() + 2 * margin_y);

		Document document = new Document(rec);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFilePath));
		// 页头页尾
		PdfHeadFoot headerFooter = new PdfHeadFoot(header, reader.getNumberOfPages(), leftBottomContent);
		writer.setPageEvent(headerFooter);
		document.open();

		PdfImportedPage page = null;
		PdfContentByte content = writer.getDirectContent();
		for (int i = 1; i <= num; i++) {
			if (i != num) {
				Rectangle lastPag = reader.getPageSizeWithRotation(i + 1);
				Rectangle lastRec = new Rectangle(lastPag.getWidth() + 2 * margin_x, lastPag.getHeight() + 2 * margin_y);
				document.setPageSize(lastRec);
			}
			page = writer.getImportedPage(reader, i);
			content.addTemplate(page, margin_x, margin_y);
			document.newPage();
		}
		document.close();
	}
}

package net.print.pdf.util;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import net.print.pdf.dto.HeadFootWithPDF;

/**
 * PDF基础工具类
 * 
 * @author David
 * @date   2017年6月15日
 */
public class PDFUtils {

	/** 打印类型(百分百) **/
	private static final int PRINT_TYPE_PERCENTAGE = 0;
	/** 打印类型(自适应) **/
	private static final int PRINT_TYPE_ADAPTIVE = 1;
	/** 是否测试模式,默认为false.(true:爬虫加载phantomPDF.js ,false:爬虫加载demo.js) **/
	public static boolean isTest = false;

	/**
	 * 创建PDF(不分页)
	 * 
	 * @param url
	 * 			要打印的页面url路径
	 * @param outPath
	 * 			输出路径
	 * @return	true:处理成功   / false:处理失败
	 */
	public static void createNotPagingPDF(String url, String outPath) throws Exception {
		createPDF(url, outPath, null, null, null);
	}

	/**
	 * 创建PDF(根据格式分页)
	 * 
	 * @param url
	 * 			要打印的页面url路径
	 * @param outPath
	 * 			输出路径
	 * @param format
	 * 			打印格式:'A3', 'A4', 'A5', 'Legal', 'Letter', 'Tabloid'.
	 * @return	true:处理成功   / false:处理失败
	 */
	public static void createPagingPDFByFormat(String url, String outPath, String format) throws Exception {
		checkFormat(format);//检查打印格式是否正确
		createPDF(url, outPath, null, null, format);
	}

	/** 检查打印格式是否正确 **/
	private static void checkFormat(String format) throws Exception {
		String[] arr = { "A3", "A4", "A5", "Legal", "Letter", "Tabloid" };
		for (String type : arr) {
			if (type.equals(format)) {
				return;
			}
		}
		throw new Exception("[PDFUtils.createPagingPDFByFormat] Cause by:printFormat=" + format + ",  printFormat must in (A3,A4,A5,Legal,Letter,Tabloid) !");
	}

	/**
	 * 创建PDF(根据宽高分页)
	 * 
	 * @param url
	 * 			要打印的页面url路径
	 * @param outPath
	 * 			输出路径
	 * @param width
	 * 			每页宽度
	 * @param height
	 * 			每页高度
	 * @return	true:处理成功   / false:处理失败
	 */
	public static void createPagingPDFByWidthAndHeight(String url, String outPath, int width, int height) throws Exception {
		checkWidthAndHeight(width, height);//检查宽高是否符合要求
		createPDF(url, outPath, width, height, null);
	}

	/** 检查宽高是否符合要求 **/
	private static void checkWidthAndHeight(int width, int height) throws Exception {
		if (width < 0 || height < 0) {
			throw new Exception("[PDFUtils.createPagingPDFByWidthAndHeight] Cause by:width=" + width + ",height=" + height + ", width and height must > 0 !");
		}
	}

	/**
	 * 创建PDF
	 * 
	 * @param url
	 * 			要打印的页面url路径
	 * @param outPath
	 * 			输出路径
	 * @param width
	 * 			每页宽度
	 * @param height
	 * 			每页高度
	 * @param format
	 * 			打印格式:'A3', 'A4', 'A5', 'Legal', 'Letter', 'Tabloid'.
	 * @return	true:处理成功   / false:处理失败
	 */
	private static void createPDF(String url, String outPath, Integer width, Integer height, String format) throws Exception {
		if (url == null || outPath == null) {
			throw new Exception("[PDFUtils.createPDF] Cause by: url is null or outPath is null !");
		}
		String[] cmd = assemblyCmd(url, outPath, width, height, format);//组装phantomjs的运行指令
		RunShell.run(cmd);
	}

	/** 组装phantomjs的运行指令 **/
	private static String[] assemblyCmd(String url, String outPath, Integer width, Integer height, String format) {
		String cmd4 = null;
		if (width != null && height != null) {
			cmd4 = width + "*" + height;
		}
		if (format != null) {
			cmd4 = format;
		}

		//组装进程命令
		String[] cmd = new String[cmd4 == null ? 4 : 5];
		cmd[0] = "phantomjs";
		cmd[1] = getPhantomJsPath();//获取爬虫Js文件路径
		cmd[2] = url;
		cmd[3] = outPath;
		if (cmd4 != null) {
			cmd[4] = cmd4;
		}
		return cmd;
	}

	/** 获取爬虫Js文件路径 **/
	private static String getPhantomJsPath() {
		//TODO 考虑各种系统中的情况
		String path = PDFUtils.class.getResource("").getPath();
		if (isTest) {
			path += "demo.js";
		} else {
			path += "phantomPDF.js";
		}
		if ("/".equals(path.substring(0, 1)) || "\\".equals(path.substring(0, 1))) {
			path = path.substring(1);
		}
		return path;
	}

	/**
	 * 缩放PDF(通过自适应)<br>
	 * [注:如果待处理PDF存在多张,则只会处理第一张,忽略其他]
	 * 
	 * @param sourcePath
	 * 			待处理文件的路径
	 * @param outPath
	 * 			输出文件的路径
	 * @param printWidth
	 * 			打印的宽度
	 * @return	true:处理成功   / false:处理失败
	 */
	public static void zoomPDByAdaptive(String sourcePath, String outPath, float printWidth) throws Exception {
		if (printWidth <= 0) {
			throw new Exception("[PDFUtils.zoomPDByAdaptive] Cause by: printWidth=" + printWidth + ", printWidth must > 0 !");
		}
		zoomPDF(sourcePath, outPath, PRINT_TYPE_ADAPTIVE, null, printWidth);
	}

	/**
	 * 缩放PDF(根据百分比)<br>
	 * [注:如果待处理PDF存在多张,则只会处理第一张,忽略其他]
	 * 
	 * @param sourcePath
	 * 			待处理文件的路径
	 * @param outPath
	 * 			输出文件的路径
	 * @param scale
	 * 			百分比(0%-200%) 格式类似: 0.1, 0.12,......
	 * @return	true:处理成功   / false:处理失败
	 */
	public static void zoomPDFByPercentage(String sourcePath, String outPath, float scale) throws Exception {
		if (scale < 0) {
			throw new Exception("[PDFUtils.zoomPDFByPercentage] Cause by:scale=" + scale + ", scale neet to 'scale>0' !");
		}
		zoomPDF(sourcePath, outPath, PRINT_TYPE_PERCENTAGE, scale, null);
	}

	/**
	 * 缩放PDF<br>
	 * [注:如果待处理PDF存在多张,则只会处理第一张,忽略其他]
	 * 
	 * @param sourcePath
	 * 			待处理文件的路径
	 * @param outPath
	 * 			输出文件的路径
	 * @param printType
	 * 			打印类型:(0:百分百;其他:自适应)
	 * @param scale
	 * 			百分比(0%-200%) 格式类似: 0.1, 0.12,......
	 * @param width
	 * 			打印的宽度
	 * @return true:处理成功   / false:处理失败
	 */
	public static void zoomPDF(String sourcePath, String outPath, int printType, Float scale, Float printWidth) throws DocumentException, IOException {

		//读取PDF
		PdfReader reader = new PdfReader(sourcePath);
		Rectangle pagesize = reader.getPageSize(1);

		float pageWidth = 600;
		float pageHeight = 600;
		float zoom = 1;
		if (printType == PRINT_TYPE_PERCENTAGE) {//百分比缩放(0%---100%)
			zoom = scale;
			pageWidth = pagesize.getWidth() * zoom;
			pageHeight = pagesize.getHeight() * zoom;
		} else {//自适应缩放
			pageWidth = printWidth;
			zoom = pageWidth / pagesize.getWidth();
			pageHeight = pagesize.getHeight() * zoom;
		}

		//开始生成PDF
		Document document = new Document(new Rectangle(pageWidth, pageHeight));
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
		document.open();
		PdfContentByte content = writer.getDirectContent();
		PdfImportedPage page = writer.getImportedPage(reader, 1);
		content.addTemplate(page, zoom, 0, 0, zoom, 0, 0);//在原页面中按纸张大小,按比率缩放,不旋转
		document.close();
		reader.close();
	}

	/**
	 * 分页PDF(根据百分百)
	 * 
	 * @param sourcePath
	 * 			待处理文件的路径
	 * @param outPath
	 * 			输出文件的路径
	 * @param printWidth
	 * 			打印的宽度
	 * @param printHeight
	 * 			打印的高度
	 * @return	true:处理成功   / false:处理失败
	 */
	public static void pagingPDFByPercentage(String sourcePath, String outPath, float printWidth, float printHeight) throws IOException, DocumentException {
		pagingPDF(sourcePath, outPath, PRINT_TYPE_PERCENTAGE, printWidth, printHeight);
	}

	/**
	 * 分页PDF(根据自适应)<br>
	 * 
	 * @param sourcePath
	 * 			待处理文件的路径
	 * @param outPath
	 * 			输出文件的路径
	 * @param printWidth
	 * 			打印的宽度
	 * @param printHeight
	 * 			打印的高度
	 * @return	true:处理成功   / false:处理失败
	 */
	public static void pagingPDFByAdaptive(String sourcePath, String outPath, float printWidth, float printHeight) throws IOException, DocumentException {
		pagingPDF(sourcePath, outPath, PRINT_TYPE_ADAPTIVE, printWidth, printHeight);
	}

	/**
	 * 分页PDF
	 * 
	 * @param sourcePath
	 * 			待处理文件的路径
	 * @param outPath
	 * 			输出文件的路径
	 * @param printType
	 * 			打印类型:(0:百分百;其他:自适应)
	 * @param printWidth
	 * 			打印的宽度
	 * @param printHeight
	 * 			打印的高度
	 * @return	true:处理成功   / false:处理失败<br><br>
	 * 
	 * 处理逻辑:<br>
	 * 	    1.检查PDF是否存在,并读取PDF<br>
	 * 		2.获取PDF第一页的内容(如果存在多页,忽略第一页以外的内容)<br>	
	 * 		3.根据需求切割第一页的内容,分成多页<br>
	 * 		4.生成PDF<br>
	 */
	public static void pagingPDF(String sourcePath, String outPath, int printType, float printWidth, float printHeight) throws IOException, DocumentException {

		PdfReader reader = new PdfReader(sourcePath);// 获取pdf文件获取实例

		// --------------------------step 1--------------------------

		Rectangle pagesize = reader.getPageSizeWithRotation(1);
		float width = pagesize.getWidth();// 爬虫生成的pdf宽度
		float height = pagesize.getHeight();// 爬虫生成的pdf高度

		int widthTotal = 1;// 宽页数
		int heightTotal = 1;// 高页数

		if (printType == PRINT_TYPE_PERCENTAGE) {// 左右分页(百分百)
			widthTotal = (int) (width / printWidth);
			if (width % printWidth > 0) {
				widthTotal++;
			}
			heightTotal = (int) (height / printHeight);
			if (height % printHeight > 0) {
				heightTotal++;
			}
		} else {// 按宽等比率缩放(自适应)
			heightTotal = (int) (height / printHeight);
			if (height % printHeight > 0) {
				heightTotal++;
			}

		}
		// 总页数
		int pageTotal = widthTotal * heightTotal;

		// --------------------------step 2--------------------------

		Rectangle re = new Rectangle(printWidth, printHeight);// 矩形实例
		Document document = new Document(re);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));

		// --------------------------step 3--------------------------
		document.open();

		// --------------------------step 4--------------------------
		PdfContentByte content = writer.getDirectContent();
		PdfImportedPage page = writer.getImportedPage(reader, 1);

		float x, y;
		for (int i = 0; i < pageTotal; i++) {
			// 从左边开始,按纸张的宽度读取内容
			x = -printWidth * (i % widthTotal);
			// 从头部开始,按纸张的高度读取内容
			y = -(height - printHeight * ((i / widthTotal) + 1));
			// 在原页面中按纸张大小,不缩放,不旋转
			content.addTemplate(page, x, y);
			document.newPage();
		}

		// --------------------------step 5--------------------------
		document.close();
		reader.close();
	}

	/**
	 * PDF添加页头
	 * 
	 * @param sourcePath
	 * 			待处理文件的路径
	 * @param outPath
	 * 			输出文件的路径
	 * @param header
	 * 			页头
	 */
	public static void addHeaderToPdf(String sourcePath, String outPath, String header) throws IOException, DocumentException {
		//注:缺省值footer不能用null, 要用""
		addHeaderFooterToPdf(sourcePath, outPath, header, "");
	}

	/**
	 * PDF添加页尾
	 * 
	 * @param sourcePath
	 * 			待处理文件的路径
	 * @param outPath
	 * 			输出文件的路径
	 * @param footer
	 * 			页脚
	 */
	public static void addFooterToPdf(String sourcePath, String outPath, String footer) throws IOException, DocumentException {
		//注:缺省值header不能用null, 要用""
		addHeaderFooterToPdf(sourcePath, outPath, "", footer);
	}

	/**
	 * PDF添加页尾
	 * 
	 * @param sourcePath
	 * 			待处理文件的路径
	 * @param outPath
	 * 			输出文件的路径
	 * @param header
	 * 			页头
	 * @param footer
	 * 			页脚
	 */
	public static void addHeaderFooterToPdf(String sourcePath, String outPath, String header, String footer) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(sourcePath);
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
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
		// 页头页尾
		HeadFootWithPDF headerFooter = new HeadFootWithPDF(header, footer, reader.getNumberOfPages());
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

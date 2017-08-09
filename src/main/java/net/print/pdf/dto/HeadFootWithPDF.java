package net.print.pdf.dto;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * PDF 页头/页尾
 * 
 * @author David
 * @date   2017年6月19日
 */
public class HeadFootWithPDF extends PdfPageEventHelper {
	/** 页头   **/
	public String header;
	/** 总页数 **/
	public int nums;
	/** 页尾(左下角) **/
	public String footer;

	/** 模板  **/
	public PdfTemplate total;
	/** 利用基础字体生成的字体对象，一般用于生成中文文字  **/
	public Font fontDetail = null;
	/** 文档字体大小，页头页脚最好和文本大小一致  **/
	public int presentFontSize = 9;
	/** 基础字体对象  **/
	public BaseFont bf = null;

	/**
	 * 初始化
	 * 
	 * @param header
	 * 			页头 
	 * @param footer
	 * 			页尾(左下角)
	 * @param nums
	 * 			总页数 
	 */
	public HeadFootWithPDF(String header, String footer, int nums) {
		this.header = header;
		this.footer = footer;
		this.nums = nums;
	}

	/**
	 * 当打开一个文档时触发，可以用于初始化文档的全局变量。
	 */
	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(50, 20);// 共 页的矩形的长宽高
	}

	/**
	 * 在创建一个新页面完成但写入内容之前触发，是添加页眉、页脚、水印等最佳时机
	 */
	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			if (bf == null) {
				String fontPath = HeadFootWithPDF.class.getResource("").getPath() + "arial-unicode-ms.ttf";
				bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			}
			if (fontDetail == null) {
				fontDetail = new Font(bf, presentFontSize, Font.ITALIC);// 数据体字体
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 注意: document.right() + document.rightMargin() 相当于pdf的宽度

		// ------------------------页头------------------------
		ColumnText.showTextAligned(writer.getDirectContent(), //
				Element.ALIGN_LEFT, //
				new Phrase(header, fontDetail), //
				5, // x坐标
				document.top() + 20, // document.top() + 20, // y坐标
				0);

		// ------------------------页脚 Page x of y pages------------------------
		ColumnText.showTextAligned(writer.getDirectContent(), //
				Element.ALIGN_LEFT, //
				new Phrase(footer, fontDetail), //
				5, //
				10, 0);

		// ------------------------页脚 Page x of y pages------------------------
		String page = "page " + writer.getPageNumber() + " of " + this.nums + " pages";
		float len = bf.getWidthPoint(page, presentFontSize);// 计算字符串的宽度
		ColumnText.showTextAligned(writer.getDirectContent(), //
				Element.ALIGN_LEFT, //
				new Phrase(page, fontDetail), //
				document.rightMargin() + document.right() - len - 5, //
				10, 0);

	}

	/**
	 * 在文档关闭之前触发，可以用于释放一些资源
	 */
	@Override
	public void onCloseDocument(PdfWriter writer, Document document) {

	}
}
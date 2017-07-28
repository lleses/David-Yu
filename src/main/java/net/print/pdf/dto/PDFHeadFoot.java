package net.print.pdf.dto;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * PDF 页头页尾
 * 
 * @author David
 * @date   2017年6月26日
 */
public class PDFHeadFoot extends PdfPageEventHelper {
	public String header;
	public float width;
	PdfTemplate total;
	Font footFont = null;

	public void setHeader(String header) {
		this.header = header;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * 当打开一个文档时触发，可以用于初始化文档的全局变量。
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(55, 16);
		try {
			BaseFont supportChinese = null;
			try {
				supportChinese = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			} catch (IOException e) {
				e.printStackTrace();
			}
			footFont = new Font(supportChinese, 9, Font.ITALIC);
		} catch (Exception e) {

		}
	}

	/**
	 * 在创建一个新页面完成但写入内容之前触发，是添加页眉、页脚、水印等最佳时机。
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		PdfPTable table = new PdfPTable(3);
		try {
			if (width < 612) {
				table.setWidths(new int[] { 29, 15, 6 });
			} else if (width < 1008) {
				table.setWidths(new int[] { 30, 15, 5 });
			} else if (width < 1191) {
				table.setWidths(new int[] { 31, 15, 4 });
			} else if (width < 1684) {
				table.setWidths(new int[] { 32, 15, 3 });
			} else if (width <= 2384) {
				table.setWidths(new int[] { 33, 15, 2 });
			} else {
				table.setWidths(new int[] { 34, 15, 1 });
			}
			table.setTotalWidth(width);
			table.setLockedWidth(true);
			table.getDefaultCell().setFixedHeight(20);
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			PdfPCell cell1 = new PdfPCell(new Phrase(header, footFont));
			cell1.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell1);

			PdfPCell cell2 = new PdfPCell(new Phrase(String.format("Page %d of ", writer.getPageNumber()), footFont));
			cell2.setBorder(Rectangle.NO_BORDER);
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell2);

			PdfPCell cell3 = new PdfPCell(Image.getInstance(total));
			cell3.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell3);

			table.writeSelectedRows(0, -1, 10, 20, writer.getDirectContent());
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	/**
	 * 在文档关闭之前触发，可以用于释放一些资源。
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(total, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber() - 1) + " pages", footFont), 2, 6, 0);
	}
}

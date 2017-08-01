//package net.print.pdf.bak;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics2D;
//import java.awt.Polygon;
//import java.awt.RenderingHints;
//import java.awt.Transparency;
//import java.awt.geom.Line2D;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URLEncoder;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Properties;
//import java.util.TimeZone;
//
//import javax.imageio.ImageIO;
//import javax.servlet.ServletOutputStream;
//
//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.FontSelector;
//import com.itextpdf.text.pdf.PdfAnnotation;
//import com.itextpdf.text.pdf.PdfBoolean;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfCopyFields;
//import com.itextpdf.text.pdf.PdfImportedPage;
//import com.itextpdf.text.pdf.PdfName;
//import com.itextpdf.text.pdf.PdfNumber;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfPageLabels;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfStamper;
//import com.itextpdf.text.pdf.PdfString;
//import com.itextpdf.text.pdf.PdfWriter;
//
//import jcifs.smb.SmbFile;
//import net.gapsystems.davinci.beans.Annotation;
//import net.gapsystems.davinci.beans.Approval;
//import net.gapsystems.davinci.beans.AttachFile;
//import net.gapsystems.davinci.beans.Shape;
//import net.gapsystems.davinci.beans.SubShape;
//import net.gapsystems.davinci.service.ConfigManager;
//import net.gapsystems.davinci.utils.DavinciLogger;
//import net.gapsystems.davinci.utils.FileUtil;
//import net.gapsystems.davinci.utils.IdentifyImage;
//
///**
// *  <p> PrintService类是所有与Print功能相关的对pdf进行底层操作的方法集合
// *  包含生成summary sheet.合并pdf文件,转换jpg为pdf,为pdf添加annotation,转换pdf对象为二进制的数组等方法.
// *  </p>
// *  @author echo
// * */
//public class PrintService {
//
//	/**
//	 * 读取指定的pdf文件并添加annotation后输出到指定路径中
//	 * @param pdfFilePath 待处理pdf文件
//	 * @param outputFilePath 处理完后输出路径
//	 * @param annoList
//	 * @param lineSize
//	 * @throws Exception
//	 */
//	public void getPdfWithAnnotationStream(String pdfFilePath, String outputFilePath, List<Annotation> annoList, int lineSize) throws Exception {
//		FileOutputStream fos = new FileOutputStream(outputFilePath);
//		PdfReader pdfReader = new PdfReader(pdfFilePath);
//		PdfStamper pdfStamper = new PdfStamper(pdfReader, fos);
//		int pageCount = pdfReader.getNumberOfPages();
//		String[] widthAndHeights = new String[pageCount];
//
//		widthAndHeights = IdentifyImage.getPdfImageWindthAndHeights(pdfReader, pageCount);
//		String wh = null;
//		if (annoList != null) {
//			for (Annotation anno : annoList) {
//				if (anno.getDependentId() != 0) {
//					continue;
//				}
//				float y = anno.getSceneY();
//				float x = anno.getSceneX();
//
//				float ay = 0f;
//				float ax = 0f;
//
//				double height = 0.0;
//				double width = 0.0;
//
//				wh = widthAndHeights[anno.getPage() - 1];
//				String[] value = wh.split(":");
//				width = Double.parseDouble(value[0]);
//				height = Double.parseDouble(value[1]);
//				float[] values = getXY(x, y, width, height, null);
//				ax = values[0];
//				ay = values[1];
//
//				PdfContentByte over = pdfStamper.getOverContent(anno.getPage());
//
//				List<Shape> shapeList = new ArrayList<Shape>();
//				Shape mShape = anno.getShape();
//				boolean isMultiselect = false;
//				if (mShape != null) {
//					if (StringUtils.isNotEmpty(mShape.getType())) {
//						if (mShape.getType().equals("multiselect")) {
//							List<SubShape> subShapeList = mShape.getSubShapes();
//							Shape multiShape = null;
//							for (SubShape subShape : subShapeList) {
//								multiShape = new Shape();
//								multiShape.setType(subShape.getType());
//								multiShape.setColor(mShape.getColor());
//								shapeList.add(multiShape);
//							}
//							isMultiselect = true;
//						}
//					}
//					if (!isMultiselect) {
//						shapeList.add(mShape);
//					}
//				} else {
//					shapeList.add(mShape);
//				}
//
//				for (Shape shape : shapeList) {
//					String[] coordinates = null;
//					if (shape != null) {
//						// 画annotation图形
//						createShape(over, shape, width, height, lineSize);
//						coordinates = shape.getType().split(" ");
//					}
//					// 如果为多annotation时获取x,y
//					if (isMultiselect && shape != null) {
//						values = getXY(Float.parseFloat(coordinates[2]), Float.parseFloat(coordinates[3]), width, height, null);
//						ax = values[0];
//						ay = values[1];
//					}
//					if (shape != null && shape.getType().indexOf("arrow") > -1) {
//						values = getXY(Float.parseFloat(coordinates[2]) + Float.parseFloat(coordinates[8]), Float.parseFloat(coordinates[3]) + Float.parseFloat(coordinates[9]), width, height, null);
//						ax = values[0];
//						ay = values[1];
//					}
//					// 画图片序号
//					createAnnotation(over, anno.getProgressiveId(), ax, ay, width, height);
//					// 添加透明的PDF note
//					createPdfAnnotation(pdfStamper, ax, ay, width, height, anno);
//				}
//			}
//		}
//		pdfStamper.close();
//		pdfReader.close();
//		fos.flush();
//		fos.close();
//	}
//
//	/** 底部的table注释 **/
//	public void createSummary(String outputFilePath, String xmlPath, String pdfjsPath, String dvserverUrl, int timezoneOffset) throws Exception {
//		String cmd = "";
//		try {
//			dvserverUrl = dvserverUrl.replace("https:", "http:");
//			dvserverUrl = dvserverUrl.replace(":443/", "/");
//			dvserverUrl += "/annoList.jsp?xmlPath=" + URLEncoder.encode(xmlPath) + "&timezoneOffset=" + timezoneOffset;
//
//			// Runtime.getRuntime().exec("wkhtmltopdf --page-size A4
//			// --orientation Landscape --margin-top 10 --margin-bottom 10
//			// --margin-left 10 --margin-right 10 "+dvserverUrl+"
//			// "+outputFilePath);
//			cmd = "phantomjs \"" + pdfjsPath + "\" \"" + dvserverUrl + "\" \"" + outputFilePath + "\" \"A4\"";
//			Runtime.getRuntime().exec(cmd);
//
//			if (!FileUtil.checkFileIsExist(outputFilePath)) {
//				throw new Exception("Can not create pdf file when use create pdf tool(" + cmd + ").");
//			}
//		} catch (Exception e) {
//			throw new Exception("Can not create pdf file when use create pdf tool(" + cmd + ").", e);
//		}
//	}
//	
//	
//	/**
//	 *  将一个pdf文件和summrySheet合并.
//	 *  pdf可能是源pdf文件的数组.也有可能是源jpg文件经过转换后生成的pdf文件的数组.
//	 *  也有可能是pdf文件添加了annotation标识后的生成的文件的数组
//	 *  
//	 *  @param byte[] pdfByteArray 待合并的pdf图像文件的byte数组
//	 *  @param byte[] summarySheetByteArray 待合并的summarySheetpdf文件的byte数组
//	 *  
//	 *  @return byte[] 合并了两个文件后生成的pdf文件的byte数组
//	 * */
//	public void mergePdfAndSummarySheet(String outputFilePath, String annoFilePath, String summarySheetFilePath) throws IOException, DocumentException {
//		FileOutputStream fos = new FileOutputStream(outputFilePath);
//		PdfReader reader1 = new PdfReader(annoFilePath);
//		PdfReader reader2 = new PdfReader(summarySheetFilePath);
//		PdfCopyFields copy = new PdfCopyFields(fos);
//
//		int pdfPage = reader1.getNumberOfPages();
//		PdfPageLabels pdfLabels = new PdfPageLabels();
//		String[] labels1 = PdfPageLabels.getPageLabels(reader1);
//		if (labels1 != null && labels1.length == pdfPage) {
//			for (int k = 0; k < labels1.length; k++) {
//				pdfLabels.addPageLabel(k + 1, PdfPageLabels.EMPTY, labels1[k], 1);
//			}
//		}
//		int summaryPage = reader2.getNumberOfPages();
//		++pdfPage;
//		for (int m = 0; m < summaryPage; m++) {
//			pdfLabels.addPageLabel(pdfPage, PdfPageLabels.EMPTY, String.valueOf(pdfPage + m), pdfPage);
//		}
//
//		copy.getWriter().setPageLabels(pdfLabels);
//		copy.open();
//		copy.addDocument(reader1);
//		copy.addDocument(reader2);
//		copy.close();
//		fos.close();
//	}
//
//}
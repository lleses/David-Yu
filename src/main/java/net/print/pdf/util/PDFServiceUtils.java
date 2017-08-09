package net.print.pdf.util;

import net.print.pdf.dto.OverviewPrintPDF;

/**
 * PDF针对具体业务的打印
 * 
 * @author David
 * @date   2017年8月9日
 */
public class PDFServiceUtils {

	/** overview打印PDF **/
	public static void overview(OverviewPrintPDF param) {
		try {
			overviewPrintPDF(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** overview打印PDF **/
	public static void overviewPrintPDF(OverviewPrintPDF param) throws Exception {
		//PDF创建
		String createPDF = param.getOutFolderPath() + "createNotPagingPDF.pdf";
		PDFUtils.createNotPagingPDF(param.getUrl(), createPDF);

		//PDF缩放
		String zoomPDF = param.getOutFolderPath() + "zoomPDF.pdf";
		PDFUtils.zoomPDF(createPDF, zoomPDF, param.getPrintType(), param.getScale(), param.getPrintWidth());

		//PDF分页
		String pagingPDF = param.getOutFolderPath() + "pagingPDF.pdf";
		PDFUtils.pagingPDF(zoomPDF, pagingPDF, param.getPrintType(), param.getPrintWidth(), param.getPrintHeight());

		//PDF添加页尾
		String addFootToPdf = param.getOutFolderPath() + "addFootToPdf.pdf";
		PDFUtils.addHeaderFooterToPdf(addFootToPdf, pagingPDF, "", param.getFooter());
	}

}

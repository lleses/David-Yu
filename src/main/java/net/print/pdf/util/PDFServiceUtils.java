package net.print.pdf.util;

import net.print.pdf.dto.OverviewPrintPDF;

/**
 * PDF针对具体业务的打印
 * 
 * @author David
 * @date   2017年8月9日
 */
public class PDFServiceUtils {

	/** 打印类型(百分百) **/
	private static final int PRINT_TYPE_PERCENTAGE = 0;

	/**
	 * overview打印PDF
	 * 
	 * @param param 
	 * 			overview打印所需要的dto参数
	 */
	public static void overview(OverviewPrintPDF param) {
		try {
			overviewPrintPDF(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** overview打印PDF **/
	private static void overviewPrintPDF(OverviewPrintPDF param) throws Exception {
		//检查overview参数是否符合业务逻辑,如果不符合则抛出错误
		checkOverviewParam(param);

		//创建PDF,如果创建失败则抛出错误
		String createPDF = param.getOutFolderPath() + "createNotPagingPDF.pdf";
		PDFUtils.createNotPagingPDF(param.getUrl(), createPDF);

		//缩放PDF,如果缩放失败则抛出错误
		String zoomPDF = param.getOutFolderPath() + "zoomPDF.pdf";
		PDFUtils.zoomPDF(createPDF, zoomPDF, param.getPrintType(), param.getScale(), param.getPrintWidth());

		//分页PDF,如果分页失败则抛出错误
		String pagingPDF = param.getOutFolderPath() + "pagingPDF.pdf";
		PDFUtils.pagingPDF(zoomPDF, pagingPDF, param.getPrintType(), param.getPrintWidth(), param.getPrintHeight());

		//添加页尾,如果添加失败则抛出错误
		String addFootToPdf = param.getOutFolderPath() + "addFootToPdf.pdf";
		PDFUtils.addHeaderFooterToPdf(pagingPDF, addFootToPdf, "", param.getFooter());
	}

	/** 检查overview参数是否符合业务逻辑,如果不符合则抛出错误 **/
	private static void checkOverviewParam(OverviewPrintPDF param) throws Exception {
		//URL不能为空
		if (param.getUrl() == null) {
			throw new Exception("[PDFServiceUtils.overview] Cause by: url is null !");
		}
		//文件夹路径不能为空
		if (param.getOutFolderPath() == null) {
			throw new Exception("[PDFServiceUtils.overview] Cause by: outFolderPath is null !");
		}
		//宽度不能为空而且要大于0
		if (param.getPrintWidth() == null || param.getPrintWidth() <= 0) {
			throw new Exception("[PDFServiceUtils.overview] Cause by: printWidth is null or printWidth<=0 !");
		}
		//高度不能为空而且要大于0
		if (param.getPrintHeight() == null || param.getPrintHeight() <= 0) {
			throw new Exception("[PDFServiceUtils.overview] Cause by: printHeight is null or printHeight<=0 !");
		}
		//当printType=0 , scale不能为空而且要在 0%-200%的区间内
		boolean checkScale = param.getScale() == null || param.getScale() < 0 || param.getScale() > 2;
		if (param.getPrintType() == PRINT_TYPE_PERCENTAGE && checkScale) {
			throw new Exception("[PDFServiceUtils.overview] Cause by:when PrintType=0, Scale is null or it not in 0%-200% !");
		}
	}

}

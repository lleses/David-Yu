package net.print.pdf.dto;

/**
 * Overview打印PDF需要填写的参数
 * 
 * @author David
 * @date   2017年6月15日
 */
public class OverviewPrintPDF {

	/** 要访问的url地址 **/
	private String url;
	/** 输出PDF的上一级文件夹路径 **/
	private String outFolderPath;
	/** 实际打印的pdf宽度 **/
	private Float printWidth;
	/** 实际打印的pdf高度 **/
	private Float printHeight;
	/** 打印类型:(0:百分百;其他:自适应) **/
	private int printType;
	/** 百分比(0%-200%) 格式类似: 0.1, 0.12,...... **/
	private Float scale;
	/** 页尾 **/
	private String footer;

	/**
	 * 要访问的url地址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 要访问的url地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 输出PDF的上一级文件夹路径
	 */
	public String getOutFolderPath() {
		return outFolderPath;
	}

	/**
	 * 输出PDF的上一级文件夹路径
	 */
	public void setOutFolderPath(String outFolderPath) {
		this.outFolderPath = outFolderPath;
	}

	/**
	 * 实际打印的pdf宽度
	 */
	public Float getPrintWidth() {
		return printWidth;
	}

	/**
	 * 实际打印的pdf宽度
	 */
	public void setPrintWidth(Float printWidth) {
		this.printWidth = printWidth;
	}

	/**
	 * 实际打印的pdf高度 
	 */
	public Float getPrintHeight() {
		return printHeight;
	}

	/**
	 * 实际打印的pdf高度 
	 */
	public void setPrintHeight(Float printHeight) {
		this.printHeight = printHeight;
	}

	/**
	 * 打印类型:(0:百分百;其他:自适应) 
	 */
	public int getPrintType() {
		return printType;
	}

	/**
	 * 打印类型:(0:百分百;其他:自适应) 
	 */
	public void setPrintType(int printType) {
		this.printType = printType;
	}

	/**
	 * 百分比(0%-200%) 格式类似: 0.1, 0.12,...... 
	 */
	public Float getScale() {
		return scale;
	}

	/**
	 * 百分比(0%-200%) 格式类似: 0.1, 0.12,...... 
	 */
	public void setScale(Float scale) {
		this.scale = scale;
	}

	/**
	 * 页尾 
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * 页尾 
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	@Override
	public String toString() {
		return "OverviewPrintPDF [url=" + url + ", outFolderPath=" + outFolderPath + ", printWidth=" + printWidth + ", printHeight=" + printHeight + ", printType=" + printType + ", scale=" + scale + ", footer=" + footer + "]";
	}

}

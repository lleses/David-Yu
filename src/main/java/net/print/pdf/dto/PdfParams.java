package net.print.pdf.dto;

import java.util.Arrays;

/**
 * PDF dto
 * 
 * @author David
 * @date   2017年6月15日
 */
@Deprecated
public class PdfParams {

	/** cmd指令 **/
	private String[] cmd;

	/** 实际打印的pdf宽度 **/
	private Float width;
	/** 实际打印的pdf高度 **/
	private Float height;
	/** 要访问的url地址 **/
	private String url;
	/** 输出路径 **/
	private String outPath;
	/** pdf父目录 **/
	private String pdfFolder;
	/** 比例类型 (0:缩放比例 , 1:原比例) **/
	private int fileScale;
	/** 缩放比例(0-100%) **/
	private Float fileWidthScale;

	/** 打印的名称 **/
	private String userName;
	/** 打印的时间 **/
	private String dateTime;
	/** PDF类型(0:不启用缩放和分页;1:启用缩放和分页) **/
	private int type = 0;

	/**
	 * 缩放比例(0-100%) 
	 */
	public Float getScale() {
		if (fileWidthScale == null) {
			return 1f;
		}
		if (0 < fileWidthScale && fileWidthScale <= 1) {
			return fileWidthScale;
		}
		if (1 < fileWidthScale && fileWidthScale <= 100) {
			return fileWidthScale / 100;
		}
		return 1f;
	}

	/**
	 * cmd指令 
	 */
	public String[] getCmd() {
		return cmd;
	}

	/**
	 * cmd指令
	 */
	public void setCmd(String[] cmd) {
		this.cmd = cmd;
	}

	/**
	 * 实际打印的pdf宽度 
	 */
	public Float getWidth() {
		return width;
	}

	/**
	 * 实际打印的pdf宽度
	 */
	public void setWidth(Float width) {
		this.width = width;
	}

	/**
	 * 实际打印的pdf高度
	 */
	public Float getHeight() {
		return height;
	}

	/**
	 * 实际打印的pdf高度
	 */
	public void setHeight(Float height) {
		this.height = height;
	}

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
	 * 输出路径
	 */
	public String getOutPath() {
		return outPath;
	}

	/**
	 * 输出路径
	 */
	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	/**
	 * 比例类型 (0:缩放比例 , 1:原比例) 
	 */
	public int getFileScale() {
		return fileScale;
	}

	/**
	 * 比例类型 (0:缩放比例 , 1:原比例) 
	 */
	public void setFileScale(int fileScale) {
		this.fileScale = fileScale;
	}

	/**
	 * 打印的名称
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 打印的名称
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 打印的时间
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * 打印的时间
	 */
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * pdf父目录 
	 */
	public String getPdfFolder() {
		return pdfFolder;
	}

	/**
	 * pdf父目录 
	 */
	public void setPdfFolder(String pdfFolder) {
		this.pdfFolder = pdfFolder;
	}

	/**
	 * 缩放比例(0-100%) 
	 */
	public Float getFileWidthScale() {
		return fileWidthScale;
	}

	/**
	 * 缩放比例(0-100%) 
	 */
	public void setFileWidthScale(Float fileWidthScale) {
		this.fileWidthScale = fileWidthScale;
	}

	/**
	 * PDF类型(0:不启用缩放和分页;1:启用缩放和分页) 
	 */
	public int getType() {
		return type;
	}

	/**
	 * PDF类型(0:不启用缩放和分页;1:启用缩放和分页) 
	 */
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PdfParams [cmd=" + Arrays.toString(cmd) + ", width=" + width + ", height=" + height + ", url=" + url + ", outPath=" + outPath + ", pdfFolder=" + pdfFolder + ", fileScale=" + fileScale + ", fileWidthScale=" + fileWidthScale + ", userName=" + userName + ", dateTime=" + dateTime + ", type=" + type + "]";
	}

}

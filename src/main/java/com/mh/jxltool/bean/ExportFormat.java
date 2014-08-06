package com.mh.jxltool.bean;

/**
 * 
 * @author jasonyao
 * 
 */
public class ExportFormat {

	private String formatId;
	private String align;
	private String vertical;
	private String dateFormat;
	private String dataFormat;
	private String width;
	private String height;
	private String fontName;
	private int fontSize = 8;
	private boolean isBlod = false;
	private boolean needWrap = true;
	private String backColor;
	private String fontColor;

	public String getFormatId() {
		return formatId;
	}

	public void setFormatId(String formatId) {
		this.formatId = formatId;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getVertical() {
		return vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public boolean isBlod() {
		return isBlod;
	}

	public void setBlod(boolean isBlod) {
		this.isBlod = isBlod;
	}

	public boolean isNeedWrap() {
		return needWrap;
	}

	public void setNeedWrap(boolean needWrap) {
		this.needWrap = needWrap;
	}

	public String getBackColor() {
		return backColor;
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

}

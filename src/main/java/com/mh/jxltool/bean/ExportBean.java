package com.mh.jxltool.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author jasonyao
 * 
 */
public class ExportBean {
	private String beanId;
	private String className;
	private int totalCols;
	private Map<Integer, ExportField> hmField = new HashMap<Integer, ExportField>();
	private Map<Integer, ExportTitle> hmTitle = new HashMap<Integer, ExportTitle>();

	public String getBeanId() {
		return beanId;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getTotalCols() {
		return totalCols;
	}

	public void setTotalCols(int totalCols) {
		this.totalCols = totalCols;
	}

	public Map<Integer, ExportField> getHmField() {
		return hmField;
	}

	public void setHmField(Map<Integer, ExportField> hmField) {
		this.hmField = hmField;
	}

	public Map<Integer, ExportTitle> getHmTitle() {
		return hmTitle;
	}

	public void setHmTitle(Map<Integer, ExportTitle> hmTitle) {
		this.hmTitle = hmTitle;
	}

	public void putExportField(int seq, ExportField field) {
		this.hmField.put(seq, field);
	}

	public void putExportTitle(int seq, ExportTitle title) {
		this.hmTitle.put(seq, title);
	}

}

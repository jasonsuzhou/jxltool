package com.mh.jxltool.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author jasonyao
 * 
 */
public class ImportBean {

	private String beanId;
	private String className;
	private int startRow;
	private int totalCols;
	private Map<Integer, ImportField> hmField = new HashMap<Integer, ImportField>();

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

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getTotalCols() {
		return totalCols;
	}

	public void setTotalCols(int totalCols) {
		this.totalCols = totalCols;
	}

	public Map<Integer, ImportField> getHmField() {
		return hmField;
	}

	public void setHmField(Map<Integer, ImportField> hmField) {
		this.hmField = hmField;
	}

	public void putImportField(int seq, ImportField field) {
		this.hmField.put(seq, field);
	}

}

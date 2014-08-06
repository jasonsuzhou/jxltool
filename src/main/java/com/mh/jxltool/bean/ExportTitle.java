package com.mh.jxltool.bean;

import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

/**
 * 
 * @author jasonyao
 * 
 */
public class ExportTitle extends AbstractExportColumn {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected void initCellFormat(ExportFormat format) throws WriteException {
		cellFormat = new WritableCellFormat();
		cellFormat.setWrap(format.isNeedWrap());
	}

}

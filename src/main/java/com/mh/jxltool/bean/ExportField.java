package com.mh.jxltool.bean;

import jxl.write.DateFormat;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author jasonyao
 * 
 */
public class ExportField extends AbstractExportColumn {

	private String getterMethod;

	public String getGetterMethod() {
		return getterMethod;
	}

	public void setGetterMethod(String getterMethod) {
		this.getterMethod = getterMethod;
	}

	protected void initCellFormat(ExportFormat format) throws WriteException {
		if (StringUtils.isNotBlank(format.getDateFormat())) {
			DateFormat df = new DateFormat(format.getDateFormat());
			cellFormat = new WritableCellFormat(df);
		} else if (StringUtils.isNotBlank(format.getDataFormat())) {
			NumberFormat nf = new NumberFormat(format.getDataFormat());
			cellFormat = new WritableCellFormat(nf);
		} else {
			cellFormat = new WritableCellFormat();
		}
	}

}

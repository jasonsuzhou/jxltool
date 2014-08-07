package com.mh.jxltool.core;

import java.util.List;

import com.mh.jxltool.bean.ExportBean;

public interface ExcelWriteUtil<T> {
	
	void generateExcel(List<T> list, ExportBean bean, String sheetName) throws Exception;

}

package com.mh.jxltool.core;

import java.util.List;

public interface ExcelReadUtil {

	String getValidationResult();

	List<Object[]> getFirstSheetRowValues() throws Exception;

}

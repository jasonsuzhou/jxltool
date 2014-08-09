package com.mh.jxltool.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.mh.jxltool.config.ImportExcelConfig;

public class JXLReadUtilTest {

	@Test
	public void getExcelData() {
		String configFilePath = "./src/test/resources/importConfig-test.xml";
		String testImportFilePath = "/Users/jasonyao/git/jxltool/test.xls";
		try {
			ImportExcelConfig.initConfig(new File(configFilePath));
			InputStream inputStream = new FileInputStream(new File(testImportFilePath));
			ExcelReadUtil util = new JXLReadUtil(inputStream, ImportExcelConfig.getImportBean("demo"));
			String validationResult = util.getValidationResult();
			if (StringUtils.isNotBlank(validationResult)) {
				// objs contains all the data inside excel
				List<Object[]> objs = util.getFirstSheetRowValues();
			} else {
				System.out.println(validationResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

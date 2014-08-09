package com.mh.jxltool.core;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mh.jxltool.bean.ExportBean;
import com.mh.jxltool.bean.ExportField;
import com.mh.jxltool.bean.ExportTitle;
import com.mh.jxltool.constant.Const;

import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class JXLWriteUtil<T> implements ExcelWriteUtil<T> {

	private OutputStream os = null;
	private WritableWorkbook wk = null;
	private Map<Integer, WritableSheet> hmSheet = new HashMap<Integer, WritableSheet>();

	public JXLWriteUtil(OutputStream os) {
		this.os = os;
	}
	
	public void generateExcel(List<T> list, ExportBean bean, String sheetName) throws Exception {
		int totalCols = bean.getTotalCols();
		this.setFirstSheetTitles(bean.getHmTitle(), sheetName, totalCols);
		this.setFirstSheetFields(list, bean.getHmField(), totalCols);
		this.generateExcel();
	}

	private WritableWorkbook getExcel() throws Exception {
		if (this.wk == null && os != null) {
			wk = Workbook.createWorkbook(os);
		}
		return wk;
	}

	private WritableSheet setFirstSheetTitles(Map<Integer, ExportTitle> titles, String sheetName, int len)
			throws Exception {
		WritableSheet sheet = this.getSheet(0, sheetName);
		if (sheet != null) {
			ExportTitle title = null;
			WritableCellFormat format = null;
			for (int i = 0; i < len; i++) {
				title = titles.get(i);
				format = title.getCellFormat();
				sheet.addCell(new Label(i, 0, title.getName(), format));
			}
		}
		return sheet;
	}
	
	private WritableSheet setFirstSheetFields(List<T> list, Map<Integer, ExportField> fields, int len) throws Exception {
		WritableSheet sheet = this.getSheet(0, null);
		Class clazz = null;
		Object obj = null;
		Method method = null;
		String returnType = null;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < len; j++) {
				clazz = list.get(i).getClass();
				method = clazz.getDeclaredMethod(fields.get(j).getGetterMethod());
				returnType = method.getReturnType().getName();
				obj = method.invoke(list.get(i));
				if (obj == null && sheet != null) {
					sheet.addCell(new Label(j, i + 1, "", fields.get(j).getCellFormat()));
				} else if (sheet != null) {
					if(Const.ReturnType.DATE.equals(returnType)) {
						sheet.addCell(new DateTime(j, i + 1, (Date)obj, fields.get(j).getCellFormat()));
					} else if (Const.ReturnType.INT.equals(returnType) || Const.ReturnType.BIG_DECIMAL.equals(returnType)
							|| Const.ReturnType.DOUBLE.equals(returnType) || Const.ReturnType.INTEGER.equals(returnType)
							) {
						sheet.addCell(new Number(j, i + 1, Double.parseDouble(String.valueOf(obj)), fields.get(j).getCellFormat()));
					} else {
						sheet.addCell(new Label(j, i + 1, String.valueOf(obj), fields.get(j).getCellFormat()));
					}
				}
			}
		}
		return sheet;
	}
	
	private void generateExcel() throws Exception {
		if (this.wk != null && this.os != null) {
			try {
				wk.write();
				wk.close();
				os.flush();
			} catch (Exception e) {
				throw e;
			} finally {
				os.close();
			}
		}
	}

	private WritableSheet getSheet(int index, String sheetName) throws Exception {
		WritableSheet sheet = this.hmSheet.get(index);
		if (sheet == null) {
			sheet = this.getExcel().createSheet(sheetName, index);
			this.hmSheet.put(index, sheet);
		}
		return sheet;
	}

}

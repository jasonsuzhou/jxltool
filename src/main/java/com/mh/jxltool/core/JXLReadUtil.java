package com.mh.jxltool.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import jxl.BooleanCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import com.mh.jxltool.bean.ImportBean;
import com.mh.jxltool.bean.ImportField;
import com.mh.jxltool.bean.ImportFieldRule;
import com.mh.jxltool.config.ImportExcelConfig;

public class JXLReadUtil implements ExcelReadUtil {

	private Workbook wk = null;
	private InputStream inputStream = null;
	private ImportBean bean = null;
	private Map<Integer, Sheet> hmSheet = new HashMap<Integer, Sheet>();
	private StringBuilder validationResult = new StringBuilder(1024);

	public JXLReadUtil(InputStream inputStream, ImportBean bean) {
		this.inputStream = inputStream;
		this.bean = bean;
	}

	public List<Object[]> getFirstSheetRowValues() throws Exception {
		Sheet sheet = this.getSheet(0);
		List<Object[]> lsObj = new ArrayList<Object[]>();
		Object[] objRowsValue = null;
		if (sheet != null) {
			int rowLen = sheet.getRows();
			int dataStartLine = this.bean.getStartRow();
			for (int i = dataStartLine; i < rowLen; i++) {
				objRowsValue = getFieldValuesViaRow(sheet, i);
				if (objRowsValue != null && objRowsValue.length > 0) {
					lsObj.add(objRowsValue);
					continue;
				} else {
					break;
				}
			}
		}
		return lsObj;
	}

	public String getValidationResult() {
		return this.validationResult.toString();
	}

	public Workbook getWorkbook() throws Exception {
		if (this.wk == null) {
			wk = Workbook.getWorkbook(this.inputStream);
		}
		return wk;
	}

	public Sheet getSheet(int index) throws Exception {
		Sheet sheet = this.hmSheet.get(index);
		if (sheet == null) {
			sheet = this.getWorkbook().getSheet(index);
			this.hmSheet.put(index, sheet);
		}
		return sheet;
	}

	public Object[] getFieldValuesViaRow(Sheet sheet, int rowIdx) {
		Cell[] rowCells = sheet.getRow(rowIdx);
		int actulLen = rowCells.length;
		int totalCols = this.bean.getTotalCols();
		int loopLen = actulLen > totalCols ? totalCols : actulLen;
		if (isEmptyRow(rowCells, loopLen)) {
			return new Object[0];
		}
		Object[] objCellsValue = getCellsValue(rowIdx, rowCells, totalCols, loopLen);
		fillEmptyValueToTheNullCell(rowIdx, actulLen, totalCols, objCellsValue);
		return objCellsValue;
	}

	private Object[] getCellsValue(int rowIdx, Cell[] rowCells, int totalCols, int loopLen) {
		List<ImportFieldRule> lsRule = null;
		ImportField field = null;
		Object[] objCellsValue = new Object[totalCols];
		for (int i = 0; i < loopLen; i++) {
			objCellsValue[i] = getCellValue(rowCells[i]);
			field = this.bean.getImportField(i);
			lsRule = field.getLsRule();
			if (lsRule != null && !lsRule.isEmpty()) {
				for (ImportFieldRule rule : lsRule) {
					this.validationResult.append(ImportExcelConfig.validate(rowCells[i].getContents(), rowCells[i],
							rule, rowIdx, i, field.getName()));
				}
			}
		}
		return objCellsValue;
	}

	private void fillEmptyValueToTheNullCell(int rowIdx, int actulLen, int totalCols, Object[] objCellsValue) {
		List<ImportFieldRule> lsRule;
		ImportField field;
		for (int i = actulLen; i < totalCols; i++) {
			objCellsValue[i] = "";
			field = this.bean.getImportField(i);
			lsRule = field.getLsRule();
			if (lsRule != null && !lsRule.isEmpty()) {
				for (ImportFieldRule rule : lsRule) {
					this.validationResult
							.append(ImportExcelConfig.validate("", null, rule, rowIdx, i, field.getName()));
				}
			}
		}
	}

	private Object getCellValue(Cell cell) {
		if (CellType.LABEL == cell.getType()) {
			LabelCell lblCell = (LabelCell) cell;
			return lblCell.getString();
		}
		if (CellType.DATE == cell.getType()) {
			DateCell dtCell = (DateCell) cell;
			return dtCell.getDate();
		}
		if (CellType.NUMBER == cell.getType()) {
			NumberCell nbCell = (NumberCell) cell;
			return nbCell.getValue();
		}
		if (CellType.BOOLEAN == cell.getType()) {
			BooleanCell bCell = (BooleanCell) cell;
			return bCell.getValue();
		}
		return null;
	}

	/**
	 * will return true if the all the cells of the row are null or empty
	 * 
	 * @param rowCells
	 * @param loopLen
	 * @return
	 */
	private boolean isEmptyRow(Cell[] rowCells, int loopLen) {
		boolean allEmpty = true;
		for (int i = 0; i < loopLen; i++) {
			if (StringUtils.isNotBlank(rowCells[i].getContents())) {
				allEmpty = false;
			}
		}
		return allEmpty;
	}

}

package com.mh.jxltool.bean;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import org.apache.commons.lang3.StringUtils;

import com.mh.jxltool.constant.StaticVariables;

public abstract class AbstractExportColumn {
	public int seq;
	public int columnWidth = 20;
	public int columnHeight = 800;
	public WritableCellFormat cellFormat = null;

	public void setFormat(ExportFormat format) throws Exception {
		initCellFormat(format);
		initBorder();
		initColumnWidth(format);
		initColumnHeight(format);
		initHorizonAlign(format);
		initVerticalAlign(format);
		initBackgroundColor(format);
		initFont(format);
	}
	
	protected abstract void initCellFormat(ExportFormat format) throws WriteException;

	private void initFont(ExportFormat format) throws WriteException {
		// TODO...The font family shall configurable also
		WritableFont font = new WritableFont(WritableFont.ARIAL, format.getFontSize(),
				format.isBlod() ? WritableFont.BOLD : WritableFont.NO_BOLD);
		if (StringUtils.isNotBlank(format.getFontColor())) {
			font.setColour(StaticVariables.getColour(format.getFontColor()));
		}
		this.cellFormat.setFont(font);
	}

	private void initBackgroundColor(ExportFormat format) throws WriteException {
		if (StringUtils.isEmpty(format.getBackColor())) {
			this.cellFormat.setBackground(Colour.GRAY_25);
		} else {
			this.cellFormat.setBackground(StaticVariables.getColour(format.getBackColor()));
		}
	}

	private void initVerticalAlign(ExportFormat format) throws WriteException {
		if (StringUtils.isBlank(format.getVertical())) {
			this.cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		} else {
			this.cellFormat.setVerticalAlignment(StaticVariables.getVertical(format.getVertical()));
		}
	}

	private void initHorizonAlign(ExportFormat format) throws WriteException {
		if (StringUtils.isBlank(format.getAlign())) {
			this.cellFormat.setAlignment(Alignment.CENTRE);
		} else {
			this.cellFormat.setAlignment(StaticVariables.getAlign(format.getAlign()));
		}
	}

	private void initColumnHeight(ExportFormat format) {
		if (StringUtils.isNotBlank(format.getHeight())) {
			this.columnHeight = Integer.parseInt(format.getHeight());
		}
	}

	private void initColumnWidth(ExportFormat format) {
		if (StringUtils.isNotBlank(format.getWidth())) {
			this.columnWidth = Integer.parseInt(format.getWidth());
		}
	}

	private void initBorder() throws WriteException {
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}

	public int getColumnHeight() {
		return columnHeight;
	}

	public void setColumnHeight(int columnHeight) {
		this.columnHeight = columnHeight;
	}

	public WritableCellFormat getCellFormat() {
		return cellFormat;
	}

	public void setCellFormat(WritableCellFormat cellFormat) {
		this.cellFormat = cellFormat;
	}

	
}

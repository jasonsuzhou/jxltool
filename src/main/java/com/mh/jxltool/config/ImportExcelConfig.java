package com.mh.jxltool.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.mh.jxltool.bean.ImportBean;
import com.mh.jxltool.bean.ImportField;
import com.mh.jxltool.bean.ImportFieldRule;
import com.mh.jxltool.constant.Const;
import com.mh.jxltool.util.Assert;
import com.mh.jxltool.util.XMLUtil;

import jxl.Cell;
import jxl.CellType;

public class ImportExcelConfig {

	private static Map<String, ImportBean> hmBean = new HashMap<String, ImportBean>();

	public static ImportBean getImportBean(String beanId) {
		return hmBean.get(beanId);
	}

	public static void initConfig(File importConfigFile) throws Exception {
		Document doc = XMLUtil.xmlFileToDom(importConfigFile);
		Element eleRoot = doc.getRootElement();
		List<Element> lsBean = XMLUtil.findChildNodes(Const.Node.BEAN, eleRoot);
		if (lsBean != null && !lsBean.isEmpty()) {
			ImportBean bean = null;
			String beanId = null;
			String start = null;
			String total = null;
			for (Element eleBean : lsBean) {
				bean = new ImportBean();
				beanId = XMLUtil.getNodeAttribute(eleBean, Const.Attr.ID);
				start = XMLUtil.getNodeAttribute(eleBean, Const.Attr.START_ROW);
				total = XMLUtil.getNodeAttribute(eleBean, Const.Attr.TOTAL_COLS);
				validation(beanId, start, total);
				bean.setBeanId(beanId);
				bean.setStartRow(Integer.parseInt(start));
				bean.setTotalCols(Integer.parseInt(total));
				initFields(bean, eleBean);
				hmBean.put(beanId, bean);
			}
		} else {
			throw new Exception("Configuration Error::Cannot find Element:[bean] under Element:[import-config]");
		}
	}

	private static void initFields(ImportBean bean, Element eleBean) throws Exception {
		List<Element> lsField = XMLUtil.findChildNodes(Const.Node.FIELD, eleBean);
		if (lsField != null && !lsField.isEmpty()) {
			ImportField field = null;
			String seq = null;
			for (Element eleField : lsField) {
				field = new ImportField();
				seq = XMLUtil.getNodeAttribute(eleField, Const.Attr.SEQUENCE);
				Assert.notBlank(seq, Assert.UNDEFINED_SEQ);
				int iseq = Integer.parseInt(seq);
				field.setSeq(iseq);
				field.setName(XMLUtil.getNodeAttribute(eleField, Const.Attr.NAME));
				field.setLsRule(initRules(eleField));
				bean.putImportField(iseq, field);
			}
		} else {
			throw new Exception("Configuration Error::Cannot find any Elements:[field] under Element:[bean].");
		}

	}

	private static List<ImportFieldRule> initRules(Element eleField) throws Exception {
		List<Element> lsEleRule = XMLUtil.findChildNodes(Const.Node.RULE, eleField);
		List<ImportFieldRule> lsRule = new ArrayList<ImportFieldRule>();
		if (lsEleRule != null && !lsEleRule.isEmpty()) {
			ImportFieldRule rule = null;
			String ruleId = null;
			for (Element eleRule : lsEleRule) {
				rule = new ImportFieldRule();
				ruleId = XMLUtil.getNodeAttribute(eleRule, Const.Attr.ID);
				Assert.notBlank(ruleId, Assert.UNDEFINED_RULE_ID);
				rule.setRuleId(Integer.parseInt(ruleId));
				rule.setValue(XMLUtil.getNodeValue(eleRule, true));
				lsRule.add(rule);
			}
		}
		return lsRule;
	}

	public static String validate(String contents, Cell cell, ImportFieldRule rule, int rowIdx, int colInx,
			String columnName) {
		int actualRow = rowIdx + 1;
		int actualCol = colInx + 1;
		StringBuilder sb = new StringBuilder();
		switch (rule.getRuleId()) {
		case Const.Rule.MANDATORY:
			if (StringUtils.isBlank(contents)) {
				sb.append(genErrorMsg(actualRow, actualCol, columnName, "is mandatory"));
			}
			break;
		case Const.Rule.MAX_LENGTH:
			int maxLen = Integer.parseInt(rule.getValue());
			if (maxLen < contents.length()) {
				sb.append(genErrorMsg(actualRow, actualCol, columnName, "shall not exceed the max length " + maxLen));
			}
			break;
		case Const.Rule.AMOUNT:
			if (StringUtils.isNotBlank(contents)) {
				if (CellType.NUMBER != cell.getType()) {
					sb.append(genErrorMsg(actualRow, actualCol, columnName, " value shall be numbric"));
				}
			}
			break;
		case Const.Rule.DATE:
			if (StringUtils.isNotBlank(contents)) {
				if (CellType.DATE != cell.getType()) {
					sb.append(genErrorMsg(actualRow, actualCol, columnName, " value shall be date format"));
				}
			}
			break;
		case Const.Rule.NUMBER:
			if (StringUtils.isNotBlank(contents)) {
				if (CellType.NUMBER != cell.getType()) {
					sb.append(genErrorMsg(actualRow, actualCol, columnName, " value shall be numbric"));
				}
			}
			break;
		case Const.Rule.FIX_LENGTH:
			int len = Integer.parseInt(rule.getValue());
			if (len != contents.length()) {
				sb.append(genErrorMsg(actualRow, actualCol, columnName, " the lengh of the value shall be " + len));

			}
			break;
		case Const.Rule.BOOLEAN:
			if (CellType.BOOLEAN != cell.getType()) {
				sb.append(genErrorMsg(actualRow, actualCol, columnName, " value shall be boolean(true or flase) format"));
			}
			break;
		default:
			break;
		}
		return sb.toString();
	}

	private static String genErrorMsg(int actualRow, int actualCol, String columnName, String message) {
		StringBuilder sb = new StringBuilder(64);
		sb.append("[Row:").append(actualRow).append(",Col:").append(actualCol).append("] \"");
		sb.append(columnName).append("\" ").append(message).append(".");
		return sb.toString();
	}

	private static void validation(String beanId, String startRow, String total) throws Exception {
		if (StringUtils.isBlank(beanId) || StringUtils.isBlank(startRow) || StringUtils.isBlank(total)) {
			throw new Exception(
					"Configuration Error::Attribute:[id][startRow][totalCols] of Element:[bean] cannot be empty.");
		}
	}

}

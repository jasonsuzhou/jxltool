package com.mh.jxltool.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.demo.XML;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.mh.jxltool.bean.AbstractExportColumn;
import com.mh.jxltool.bean.ExportBean;
import com.mh.jxltool.bean.ExportField;
import com.mh.jxltool.bean.ExportFormat;
import com.mh.jxltool.bean.ExportTitle;
import com.mh.jxltool.constant.Const;
import com.mh.jxltool.util.Assert;
import com.mh.jxltool.util.XMLUtil;

public class ExportExcelConfig {

	private static Map<String, ExportBean> hmBean = new HashMap<String, ExportBean>();
	private static Map<String, ExportFormat> hmFormat = new HashMap<String, ExportFormat>();

	public static ExportBean getExportBean(String beanId) {
		return hmBean.get(beanId);
	}

	public static void initConfig(File exportConfig) throws Exception {
		Document doc = XMLUtil.xmlFileToDom(exportConfig);
		Element eleRoot = doc.getRootElement();
		initFormats(eleRoot);
		List<Element> lsEleBean = XMLUtil.findChildNodes(Const.Node.BEAN, eleRoot);
		if (lsEleBean != null && !lsEleBean.isEmpty()) {
			ExportBean bean = null;
			String beanId = null;
			String total = null;
			String className = null;
			for (Element eleBean : lsEleBean) {
				beanId = XMLUtil.getNodeAttribute(eleBean, Const.Attr.ID);
				className = XMLUtil.getNodeAttribute(eleBean, Const.Attr.CLASS_NAME);
				total = XMLUtil.getNodeAttribute(eleBean, Const.Attr.TOTAL_COLS);
				validation(beanId, total, className);
				bean = new ExportBean();
				bean.setBeanId(beanId);
				bean.setClassName(className);
				bean.setTotalCols(Integer.parseInt(total));
				initTitles(bean, eleBean);
				initFields(bean, eleBean);
				hmBean.put(beanId, bean);
			}
		} else {
			throw new Exception("Configuration Error::Cannot find Element:[bean] under Element:[export-config]");
		}
	}

	private static void initFields(ExportBean bean, Element eleBean) throws Exception {
		Element eleFields = XMLUtil.findChildNode(Const.Node.FIELDS, eleBean);
		if (eleFields != null) {
			String fieldRefFormat = XMLUtil.getNodeAttribute(eleFields, Const.Attr.REFERENCE);
			List<Element> lsEleField = XMLUtil.findChildNodes(Const.Node.FIELD, eleFields);
			initField(bean, fieldRefFormat, lsEleField);
		} else {
			throw new Exception("Configuration Error::Cannot find Element:[fields] under Element:[bean].");
		}

	}

	private static void initField(ExportBean bean, String fieldRefFormat, List<Element> lsEleField) throws Exception {
		if (lsEleField != null && !lsEleField.isEmpty()) {
			ExportField field = null;
			String seq = null;
			for (Element eleField : lsEleField) {
				field = new ExportField();
				seq = XMLUtil.getNodeAttribute(eleField, Const.Attr.SEQUENCE);
				if (StringUtils.isNotBlank(seq)) {
					int iseq = Integer.parseInt(seq);
					field.setSeq(iseq);
					field.setGetterMethod(XMLUtil.getNodeAttribute(eleField, Const.Attr.GETTER_METHOD));
					chooseFieldFormat(fieldRefFormat, field, eleField);
					bean.putExportField(iseq, field);
				} else {
					throw new Exception(Assert.UNDEFINED_SEQ);
				}
			}
		}

	}

	private static void chooseTitleFormat(String titlesRefFormat, ExportTitle title, Element eleTitle) throws Exception {
		chooseColumnFormat(titlesRefFormat, title, eleTitle);
	}

	private static void chooseFieldFormat(String fieldRefFormat, ExportField field, Element eleField) throws Exception {
		chooseColumnFormat(fieldRefFormat, field, eleField);
	}

	private static void chooseColumnFormat(String refFormat, AbstractExportColumn column, Element eleColumn)
			throws Exception {
		if (StringUtils.isNotEmpty(refFormat)) {
			ExportFormat format = hmFormat.get(refFormat);
			if (format != null) {
				column.setFormat(format);
			} else {
				throw new Exception("Configuration Error::Cannot find the refered format for Attribute:[ref]:"
						+ refFormat);
			}
		} else {
			Element eleFormat = XMLUtil.findChildNode(Const.Node.FORMAT, eleColumn);
			String ref = XMLUtil.getNodeAttribute(eleFormat, Const.Attr.REFERENCE);
			if (StringUtils.isNotBlank(ref)) {
				ExportFormat format = hmFormat.get(ref);
				if (format != null) {
					column.setFormat(format);
				} else {
					throw new Exception("Configuration Error::Cannot find the refered format for Attribute:[ref]:"
							+ refFormat);
				}
			} else {
				column.setFormat(initFormat(eleFormat));
			}
		}
	}

	private static void initTitles(ExportBean bean, Element eleBean) throws Exception {
		Element eleTitles = XMLUtil.findChildNode(Const.Node.TITLES, eleBean);
		if (eleTitles != null) {
			String titlesRefFormat = XMLUtil.getNodeAttribute(eleTitles, Const.Attr.REFERENCE);
			List<Element> lsEleTitle = XMLUtil.findChildNodes(Const.Node.TITLE, eleTitles);
			initTitle(bean, titlesRefFormat, lsEleTitle);
		} else {
			throw new Exception("Configuration Error::Cannot find Element:[titles] under Element:[bean].");
		}

	}

	private static void initTitle(ExportBean bean, String titlesRefFormat, List<Element> lsEleTitle) throws Exception {
		if (lsEleTitle != null && !lsEleTitle.isEmpty()) {
			ExportTitle title = null;
			String seq = null;
			for (Element eleTitle : lsEleTitle) {
				title = new ExportTitle();
				title.setName(XMLUtil.getNodeAttribute(eleTitle, Const.Attr.NAME));
				seq = XMLUtil.getNodeAttribute(eleTitle, Const.Attr.SEQUENCE);
				if (StringUtils.isBlank(seq)) {
					throw new Exception("Configuration Error::Attribute:[seq] of Element:[title] is undefined.");
				}
				int iseq = Integer.parseInt(seq);
				title.setSeq(iseq);
				chooseTitleFormat(titlesRefFormat, title, eleTitle);
				bean.putExportTitle(iseq, title);
			}
		}
	}

	/**
	 * loop the [formats] element inside the configure file and put into
	 * hmFormat
	 */
	private static void initFormats(Element eleRoot) {
		Element eleFormats = XMLUtil.findChildNode(Const.Node.FORMATS, eleRoot);
		if (eleFormats != null) {
			List<Element> lsEleFormat = XMLUtil.findChildNodes(Const.Node.FORMAT, eleFormats);
			if (lsEleFormat != null && !lsEleFormat.isEmpty()) {
				for (Element eleFormat : lsEleFormat) {
					initFormat(eleFormat);
				}
			}
		}
	}

	private static ExportFormat initFormat(Element eleFormat) {
		ExportFormat format = new ExportFormat();
		String formatId = XMLUtil.getNodeAttribute(eleFormat, Const.Attr.ID);
		format.setFormatId(formatId);
		format.setAlign(XMLUtil.getChildNodeValue(eleFormat, Const.Node.ALIGN, true));
		format.setBackColor(XMLUtil.getChildNodeValue(eleFormat, Const.Node.BACKGROUND_COLOR, true));
		format.setDateFormat(XMLUtil.getChildNodeValue(eleFormat, Const.Node.DATE_FORMAT, true));
		format.setDataFormat(XMLUtil.getChildNodeValue(eleFormat, Const.Node.DATA_FORMAT, true));
		format.setFontName(XMLUtil.getChildNodeValue(eleFormat, Const.Node.FONT_NAME, true));
		format.setFontColor(XMLUtil.getChildNodeValue(eleFormat, Const.Node.FONT_COLOR, true));
		format.setHeight(XMLUtil.getChildNodeValue(eleFormat, Const.Node.HEIGHT, true));
		format.setWidth(XMLUtil.getChildNodeValue(eleFormat, Const.Node.WIDTH, true));
		String fontSize = XMLUtil.getChildNodeValue(eleFormat, Const.Node.FONT_SIZE, true);
		if (StringUtils.isNotBlank(fontSize)) {
			format.setFontSize(Integer.parseInt(fontSize));
		}
		format.setVertical(XMLUtil.getChildNodeValue(eleFormat, Const.Node.VERTICAL_ALIGN, true));
		String bold = XMLUtil.getChildNodeValue(eleFormat, Const.Node.BLOD, true);
		if (StringUtils.isNotBlank(bold)) {
			format.setBlod(Boolean.valueOf(bold));
		}
		String wrap = XMLUtil.getChildNodeValue(eleFormat, Const.Node.WRAP, true);
		if (StringUtils.isNotBlank(wrap)) {
			format.setNeedWrap(Boolean.valueOf(wrap));
		}
		hmFormat.put(formatId, format);
		return format;
	}

	private static void validation(String beanId, String total, String className) throws Exception {
		if (StringUtils.isBlank(beanId) || StringUtils.isBlank(className) || StringUtils.isBlank(total)) {
			throw new Exception(
					"Configuration Error::Attribute:[id][class][totalCols] of Element:[bean] cannot be empty.");
		}
	}
}

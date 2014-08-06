package com.mh.jxltool.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.demo.XML;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.mh.jxltool.bean.ExportBean;
import com.mh.jxltool.bean.ExportFormat;
import com.mh.jxltool.bean.ExportTitle;
import com.mh.jxltool.constant.Const;
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

	private static void initField(ExportBean bean, String fieldRefFormat, List<Element> lsEleField) {
		// TODO Auto-generated method stub
		
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

	private static void chooseTitleFormat(String titlesRefFormat, ExportTitle title, Element eleTitle) throws Exception {
		if (StringUtils.isNotEmpty(titlesRefFormat)) {
			ExportFormat format = hmFormat.get(titlesRefFormat);
			if (format != null) {
				title.setFormat(format);
			} else {
				throw new Exception("Configuration Error::Cannot find the refred format for Attribute:[ref]:"
						+ titlesRefFormat);
			}
		} else {
			Element eleFormat = XMLUtil.findChildNode(Const.Node.FORMAT, eleTitle);
			String ref = XMLUtil.getNodeAttribute(eleFormat, Const.Attr.REFERENCE);
			if (StringUtils.isNotBlank(ref)) {
				ExportFormat format = hmFormat.get(ref);
				if (format != null) {
					title.setFormat(format);
				} else {
					throw new Exception("Configuration Error::Cannot find the refred format for Attribute:[ref]:"
							+ titlesRefFormat);
				}
			} else {
				title.setFormat(initFormat(eleFormat));
			}
		}

	}

	private static void validation(String beanId, String total, String className) throws Exception {
		if (StringUtils.isBlank(beanId) || StringUtils.isBlank(className) || StringUtils.isBlank(total)) {
			throw new Exception(
					"Configuration Error::Attribute:[id][class][totalCols] of Element:[bean] cannot be empty.");
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
		// TODO Auto-generated method stub
		return null;
	}
}

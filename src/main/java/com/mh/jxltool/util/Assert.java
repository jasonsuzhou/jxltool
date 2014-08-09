package com.mh.jxltool.util;

import org.apache.commons.lang3.StringUtils;

public class Assert {
	public static final String UNDEFINED_SEQ = "Configuration Error::Attribute:[seq] of Element:[field] is undefined.";
	public static final String UNDEFINED_RULE_ID = "Configuration Error::Attribute:[id] of Element:[rule] is undefined.";
	
	public static void notBlank(String value, String message) throws Exception {
		if (StringUtils.isBlank(value)) {
			throw new Exception(message);
		}
	}

}

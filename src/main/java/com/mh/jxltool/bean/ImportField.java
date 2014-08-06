package com.mh.jxltool.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jasonyao
 * 
 */
public class ImportField {

	private int seq;
	private String name;
	private List<ImportFieldRule> lsRule = new ArrayList<ImportFieldRule>();

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ImportFieldRule> getLsRule() {
		return lsRule;
	}

	public void setLsRule(List<ImportFieldRule> lsRule) {
		this.lsRule = lsRule;
	}

	public void putFieldRule(ImportFieldRule rule) {
		this.lsRule.add(rule);
	}

}

package com.hose.model;

import java.util.List;

public class Filters {
	private String groupOp;
	List<FilterItem> rules;
	
	public String getGroupOp() {
		return groupOp;
	}
	public void setGroupOp(String groupOp) {
		this.groupOp = groupOp;
	}
	public List<FilterItem> getRules() {
		return rules;
	}
	public void setRules(List<FilterItem> rules) {
		this.rules = rules;
	}
}

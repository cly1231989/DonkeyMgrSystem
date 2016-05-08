package com.hose.model;

import java.util.List;

public class UserPageInfo {
	public long total;
	public long page;
	public long records;
	
	public long id;
	List<User> rows;
	
	public UserPageInfo(long total, long page, long records, long id, List<User> rows) {
		super();
		this.total = total;
		this.page = page;
		this.records = records;
		this.id = id;
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getRecords() {
		return records;
	}

	public void setRecords(long records) {
		this.records = records;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<User> getRows() {
		return rows;
	}

	public void setRows(List<User> rows) {
		this.rows = rows;
	}
}

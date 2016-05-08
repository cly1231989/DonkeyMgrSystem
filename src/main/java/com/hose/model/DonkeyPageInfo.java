package com.hose.model;

import java.util.List;

import org.springframework.data.domain.Page;

public class DonkeyPageInfo {
	public long total;
	public long page;
	public long records;
	
	public long id;
	List<Donkey> rows;
	
	public DonkeyPageInfo(long total, long page, long records, long id, List<Donkey> rows) {
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

	public List<Donkey> getRows() {
		return rows;
	}

	public void setRows(List<Donkey> rows) {
		this.rows = rows;
	}
}

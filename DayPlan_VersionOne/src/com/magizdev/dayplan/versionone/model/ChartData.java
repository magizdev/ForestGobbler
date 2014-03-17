package com.magizdev.dayplan.versionone.model;

public class ChartData {
	public long biid;
	public String backlogName;
	public int data;
	public float fdata;

	public ChartData() {
	}

	public ChartData(long id, String name, int data) {
		this.biid = id;
		this.backlogName = name;
		this.data = data;
	}

	public ChartData(long id, String name, float fdata) {
		this.biid = id;
		this.backlogName = name;
		this.fdata = fdata;
	}
}
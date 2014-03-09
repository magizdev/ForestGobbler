package com.magizdev.dayplan.versionone;

public class PieChartData {
	public long biid;
	public String backlogName;
	public int data;

	public PieChartData() {
	}
	
	public PieChartData(long id, String name, int data){
		this.biid = id;
		this.backlogName = name;
		this.data = data;
	}
}
package com.magizdev.babyoneday.viewmodel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name="GrowthIndex")
public class GrowthIndexInfo extends Model {
	public final static int HEIGHT=1;
	public static final int WEIGHT=2;
	public static final int HEAD_GIRTH=3;
	
	@Column(name="indexType")
	public int IndexType;
	
	@Column(name="date")
	public int Date;
	
	@Column(name="value")
	public float Value;
	
	
}

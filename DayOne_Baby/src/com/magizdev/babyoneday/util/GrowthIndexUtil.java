package com.magizdev.babyoneday.util;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

import com.activeandroid.query.Select;
import com.magizdev.babyoneday.viewmodel.GrowthIndexInfo;

public class GrowthIndexUtil {
	public static final int GENDER_BOY = 1;
	public static final int GENDER_GIRL = 2;
	public static final int GI_HEIGHT = 0;
	public static final int GI_WEIGHT = 1;
	public static final int GI_LOW = 0;
	public static final int GI_HEIGH = 1;

	public static void addRecord(int indexType, float data, int date) {
		GrowthIndexInfo newRecord = new GrowthIndexInfo();
		newRecord.IndexType = indexType;
		newRecord.Date = date;
		newRecord.Value = data;
		newRecord.save();
	}

	static List<Pair<Integer, Float>> boy_height_low;
	static List<Pair<Integer, Float>> boy_height_high;
	static List<Pair<Integer, Float>> boy_weight_low;
	static List<Pair<Integer, Float>> boy_weight_high;

	static List<Pair<Integer, Float>> girl_height_low;
	static List<Pair<Integer, Float>> girl_height_high;
	static List<Pair<Integer, Float>> girl_weight_low;
	static List<Pair<Integer, Float>> girl_weight_high;

	static {
		boy_height_low = new ArrayList<Pair<Integer, Float>>();
		boy_height_low.add(new Pair<Integer, Float>(0, 48.2f));
		boy_height_low.add(new Pair<Integer, Float>(1, 52.1f));
		boy_height_low.add(new Pair<Integer, Float>(2, 55.5f));
		boy_height_low.add(new Pair<Integer, Float>(3, 58.5f));
		boy_height_low.add(new Pair<Integer, Float>(4, 61.0f));
		boy_height_low.add(new Pair<Integer, Float>(5, 63.2f));
		boy_height_low.add(new Pair<Integer, Float>(6, 65.1f));
		boy_height_low.add(new Pair<Integer, Float>(8, 68.3f));
		boy_height_low.add(new Pair<Integer, Float>(10, 71.0f));
		boy_height_low.add(new Pair<Integer, Float>(12, 73.4f));

		boy_height_high = new ArrayList<Pair<Integer, Float>>();
		boy_height_high.add(new Pair<Integer, Float>(0, 52.8f));
		boy_height_high.add(new Pair<Integer, Float>(1, 57.0f));
		boy_height_high.add(new Pair<Integer, Float>(2, 60.7f));
		boy_height_high.add(new Pair<Integer, Float>(3, 63.7f));
		boy_height_high.add(new Pair<Integer, Float>(4, 66.4f));
		boy_height_high.add(new Pair<Integer, Float>(5, 68.6f));
		boy_height_high.add(new Pair<Integer, Float>(6, 70.5f));
		boy_height_high.add(new Pair<Integer, Float>(8, 73.6f));
		boy_height_high.add(new Pair<Integer, Float>(10, 76.3f));
		boy_height_high.add(new Pair<Integer, Float>(12, 78.8f));

		boy_weight_low = new ArrayList<Pair<Integer, Float>>();
		boy_weight_low.add(new Pair<Integer, Float>(0, 2.9f));
		boy_weight_low.add(new Pair<Integer, Float>(1, 3.6f));
		boy_weight_low.add(new Pair<Integer, Float>(2, 4.3f));
		boy_weight_low.add(new Pair<Integer, Float>(3, 5.0f));
		boy_weight_low.add(new Pair<Integer, Float>(4, 5.7f));
		boy_weight_low.add(new Pair<Integer, Float>(5, 6.3f));
		boy_weight_low.add(new Pair<Integer, Float>(6, 6.9f));
		boy_weight_low.add(new Pair<Integer, Float>(8, 7.8f));
		boy_weight_low.add(new Pair<Integer, Float>(10, 8.6f));
		boy_weight_low.add(new Pair<Integer, Float>(12, 9.1f));

		boy_weight_high = new ArrayList<Pair<Integer, Float>>();
		boy_weight_high.add(new Pair<Integer, Float>(0, 3.8f));
		boy_weight_high.add(new Pair<Integer, Float>(1, 5.0f));
		boy_weight_high.add(new Pair<Integer, Float>(2, 6.0f));
		boy_weight_high.add(new Pair<Integer, Float>(3, 6.9f));
		boy_weight_high.add(new Pair<Integer, Float>(4, 7.6f));
		boy_weight_high.add(new Pair<Integer, Float>(5, 8.2f));
		boy_weight_high.add(new Pair<Integer, Float>(6, 8.8f));
		boy_weight_high.add(new Pair<Integer, Float>(8, 9.8f));
		boy_weight_high.add(new Pair<Integer, Float>(10, 10.6f));
		boy_weight_high.add(new Pair<Integer, Float>(12, 11.3f));

		girl_height_low = new ArrayList<Pair<Integer, Float>>();
		girl_height_low.add(new Pair<Integer, Float>(0, 47.7f));
		girl_height_low.add(new Pair<Integer, Float>(1, 51.2f));
		girl_height_low.add(new Pair<Integer, Float>(2, 54.4f));
		girl_height_low.add(new Pair<Integer, Float>(3, 57.1f));
		girl_height_low.add(new Pair<Integer, Float>(4, 59.4f));
		girl_height_low.add(new Pair<Integer, Float>(5, 61.5f));
		girl_height_low.add(new Pair<Integer, Float>(6, 63.3f));
		girl_height_low.add(new Pair<Integer, Float>(8, 66.4f));
		girl_height_low.add(new Pair<Integer, Float>(10, 69.0f));
		girl_height_low.add(new Pair<Integer, Float>(12, 71.5f));

		girl_height_high = new ArrayList<Pair<Integer, Float>>();
		girl_weight_low = new ArrayList<Pair<Integer, Float>>();
		girl_weight_high = new ArrayList<Pair<Integer, Float>>();

		add(0, 52f, 2.7f, 3.6f);
		add(1, 55.8f, 3.4f, 4.5f);
		add(2, 59.2f, 4.0f, 5.4f);
		add(3, 59.5f, 4.7f, 6.2f);
		add(4, 64.5f, 5.3f, 6.9f);
		add(5, 66.7f, 5.8f, 7.5f);
		add(6, 68.6f, 6.3f, 8.1f);
		add(8, 71.8f, 7.2f, 9.1f);
		add(10, 74.5f, 7.9f, 9.9f);
		add(12, 77.1f, 8.5f, 10.6f);
	}

	static void add(int month, Float girlHeightHigh, Float girlWeightLow,
			Float girlWeightHigh) {
		girl_height_high.add(new Pair<Integer, Float>(month, girlHeightHigh));
		girl_weight_high.add(new Pair<Integer, Float>(month, girlWeightHigh));
		girl_weight_low.add(new Pair<Integer, Float>(month, girlWeightLow));
	}

	public static List<Pair<Integer, Float>> getStandard(int gender,
			int indexType, int standardType) {
		if (gender == GENDER_BOY) {
			if (indexType == GI_HEIGHT) {
				if (standardType == GI_LOW) {
					return boy_height_low;
				} else {
					return boy_height_high;
				}
			} else {
				if (standardType == GI_LOW) {
					return boy_weight_low;
				} else {
					return boy_weight_high;
				}
			}
		} else {
			if (indexType == GI_HEIGHT) {
				if (standardType == GI_LOW) {
					return girl_height_low;
				} else {
					return girl_height_high;
				}
			} else {
				if (standardType == GI_LOW) {
					return girl_weight_low;
				} else {
					return girl_weight_high;
				}
			}
		}
	}

	public static List<Pair<Integer, Float>> getProfile(int indexType) {
		int birthDate = Profile.Instance().birthday;
		List<GrowthIndexInfo> profileData = new Select()
				.from(GrowthIndexInfo.class).where("indexType = " + indexType)
				.execute();
		List<Pair<Integer, Float>> result = new ArrayList<Pair<Integer, Float>>();
		for (GrowthIndexInfo data : profileData) {
			result.add(new Pair<Integer, Float>(data.Date - birthDate,
					data.Value));
		}

		return result;
	}
}

package com.magizdev.dayplan.versionone.util;

import java.util.Comparator;

import com.magizdev.dayplan.versionone.model.BacklogItem;

public class BacklogComparator implements Comparator<BacklogItem> {

	@Override
	public int compare(BacklogItem lhs, BacklogItem rhs) {

		return getweight(rhs) - getweight(lhs);
	}

	private int getweight(BacklogItem backlogItem) {
		float weight = 0;
		if (backlogItem.HasDueDate()) {
			if (backlogItem.DueDate > DayUtil.Today()) {
				weight += 1f / (backlogItem.DueDate - DayUtil.Today());

				if (backlogItem.HasEstimate()) {
					weight += backlogItem.RemainEstimate
							/ (backlogItem.DueDate - DayUtil.Today());
				}
			} else {
				weight += 100;
			}
		}

		return (int) (weight * 100);
	}
}

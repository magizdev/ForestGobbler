package com.magizdev.easytask;

import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class EasyGestureListener extends
		GestureDetector.SimpleOnGestureListener {
	private ListView listView;
	private Handler uiHandler;
	private long animDuration;

	public EasyGestureListener(ListView listView, Handler uiHandler, long animDuration) {
		this.listView = listView;
		this.uiHandler = uiHandler;
		this.animDuration = animDuration;
	}

	@Override
	public boolean onDown(MotionEvent event) {

		return true;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
			float velocityX, float velocityY) {
		int positionDown = listView.pointToPosition((int) event1.getX(),
				(int) event1.getY());
		int positionUp = listView.pointToPosition((int) event2.getX(),
				(int) event2.getY());
		if (positionDown == positionUp
				&& positionDown != ListView.INVALID_POSITION){
			int visiblePosition = positionDown - listView.getFirstVisiblePosition();
			Button deleteBtn = (Button) listView.getChildAt(visiblePosition).findViewById(R.id.deleteBtn);
			deleteBtn.setAlpha(0);
			deleteBtn.setVisibility(View.VISIBLE);
			float origx = deleteBtn.getScaleX();
			deleteBtn.setScaleX(0);
			deleteBtn.setPivotX(origx);
			deleteBtn.animate().alpha(1).scaleX(origx)
					.setDuration(animDuration).setListener(null).start();
			uiHandler.sendEmptyMessageDelayed(positionDown, 2000);
		}
			return true;
	}
}

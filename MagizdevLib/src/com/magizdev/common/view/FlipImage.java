package com.magizdev.common.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.magizdev.common.lib.R;

public class FlipImage extends ImageView implements AnimationListener {
	private boolean isFrontUp = false;

	private Drawable frontPic;
	private Drawable backPic;

	public FlipImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
	}

	public void setFrontPic(int id) {
		frontPic = loadResource(id);
	}

	public void setBackPic(int id) {
		backPic = loadResource(id);
	}

	private Drawable loadResource(int id) {
		return getResources().getDrawable(id);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		flip();
		return super.onTouchEvent(event);
	}

	private void flip() {
		Animation bounceAnimation = AnimationUtils.loadAnimation(getContext(),
				R.anim.flip_in);
		bounceAnimation.setAnimationListener(this);
		this.startAnimation(bounceAnimation);
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (isFrontUp) {
			this.setImageDrawable(backPic);
		} else {
			this.setImageDrawable(frontPic);
		}
		isFrontUp = !isFrontUp;
		Animation bounceOutAnimation = AnimationUtils.loadAnimation(
				getContext(), R.anim.flip_out);
		this.startAnimation(bounceOutAnimation);

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}
}

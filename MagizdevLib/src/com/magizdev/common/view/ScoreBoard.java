package com.magizdev.common.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magizdev.common.lib.IScoreListener;
import com.magizdev.common.lib.R;

public class ScoreBoard extends ViewGroup implements IScoreListener {
	private TextView totalScore;
	private Context context;
	private LinearLayout linearLayout;

	public ScoreBoard(Context context) {
		super(context);
		this.context = context;
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	public ScoreBoard(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		inflate(context, R.layout.score_board, this);
		totalScore = (TextView) findViewById(R.id.totalScore);
		linearLayout = (LinearLayout) findViewById(R.id.scoreBoardRoot);
		linearLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		linearLayout.layout(r - linearLayout.getMeasuredWidth() - linearLayout.getPaddingRight(), 0, r,
				linearLayout.getMeasuredHeight());

	}

	public void setTotalScore(int score) {
		totalScore.setText(String.format("%1$06d", score));
	}

	@Override
	public void OnScoreChanged(int score) {
		setTotalScore(score);
	}
}

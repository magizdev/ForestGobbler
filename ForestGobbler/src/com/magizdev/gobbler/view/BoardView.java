package com.magizdev.gobbler.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.magizdev.gobbler.FeatureSettingUtil;
import com.magizdev.gobbler.R;
import com.magizdev.gobbler.view.GameModel.Node;

public class BoardView extends View {

	protected int iconSize;
	protected int iconCounts = 17;
	protected Bitmap[] icons = new Bitmap[iconCounts];
	private Bitmap selectBox;
	protected Node selected = null;
	protected GameModel gameModel;
	protected FeatureSettingUtil featureUtil;
	private Random r;

	public BoardView(Context context, AttributeSet atts) {
		super(context, atts);
		featureUtil = new FeatureSettingUtil(context);
		r = new Random();
	}

	public void setBoardSize(int xCount, int yCount) {
		gameModel = new GameModel(xCount, yCount, iconCounts);
		gameModel.init();

		List<Integer> enabledGravity = new ArrayList<Integer>();
		for (int id : featureUtil.getFeatures()) {
			if (featureUtil.getFeature(id)) {
				enabledGravity.add(id);
			}
		}

		if (enabledGravity.size() > 0) {
			int gravityId = enabledGravity
					.get(r.nextInt(enabledGravity.size()));
			switch (gravityId) {
			case R.string.feature_gravity_down:
				gameModel.setGravity(GameModel.GRAVITY_DOWN);
				break;
			case R.string.feature_gravity_left:
				gameModel.setGravity(GameModel.GRAVITY_LEFT);
				break;
			case R.string.feature_gravity_right:
				gameModel.setGravity(GameModel.GRAVITY_RIGHT);
				break;
			case R.string.feature_gravity_up:
				gameModel.setGravity(GameModel.GRAVITY_UP);
				break;
			}
		}

		calIconSize(xCount);

		Resources r = getResources();
		loadBitmaps(1, r.getDrawable(R.drawable.animal_1));
		loadBitmaps(2, r.getDrawable(R.drawable.animal_2));
		loadBitmaps(3, r.getDrawable(R.drawable.animal_3));
		loadBitmaps(4, r.getDrawable(R.drawable.animal_4));
		loadBitmaps(5, r.getDrawable(R.drawable.animal_5));
		loadBitmaps(6, r.getDrawable(R.drawable.animal_6));
		loadBitmaps(7, r.getDrawable(R.drawable.animal_7));
		loadBitmaps(8, r.getDrawable(R.drawable.animal_8));
		loadBitmaps(9, r.getDrawable(R.drawable.animal_9));
		loadBitmaps(10, r.getDrawable(R.drawable.animal_10));
		loadBitmaps(11, r.getDrawable(R.drawable.animal_11));
		loadBitmaps(12, r.getDrawable(R.drawable.animal_12));
		loadBitmaps(13, r.getDrawable(R.drawable.animal_13));
		loadBitmaps(14, r.getDrawable(R.drawable.animal_14));
		loadBitmaps(15, r.getDrawable(R.drawable.animal_15));
		loadBitmaps(16, r.getDrawable(R.drawable.animal_16));
		selectBox = DrawableToBitmap(r.getDrawable(R.drawable.selected));
	}

	private void calIconSize(int xCount) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) this.getContext()).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		iconSize = dm.widthPixels / (xCount);
	}

	public void loadBitmaps(int key, Drawable d) {
		icons[key] = DrawableToBitmap(d);
	}
	
	private Bitmap DrawableToBitmap(Drawable d){
		Bitmap bitmap = Bitmap.createBitmap(iconSize, iconSize,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		d.setBounds(0, 0, iconSize, iconSize);
		d.draw(canvas);
		return bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		List<Node> path = gameModel.getPath();
		if (path != null && path.size() >= 2) {
			for (int i = 0; i < path.size() - 1; i++) {
				Paint paint = new Paint();
				paint.setColor(Color.CYAN);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(3);
				Point p1 = indextoScreen(path.get(i).x, path.get(i).y);
				Point p2 = indextoScreen(path.get(i + 1).x, path.get(i + 1).y);
				canvas.drawLine(p1.x + iconSize / 2, p1.y + iconSize / 2, p2.x
						+ iconSize / 2, p2.y + iconSize / 2, paint);
			}
			Node n1 = path.get(0);
			Node n2 = path.get(path.size() - 1);
			gameModel.clear(n1, n2);
			gameModel.getPath().clear();
			selected = null;
		}
		for (int x = 0; x < gameModel.getHeight(); x++) {
			for (int y = 0; y < gameModel.getWidth(); y++) {
				if (gameModel.getNode(x, y) > 0) {
					Point p = indextoScreen(x, y);
					if (featureUtil.getFeature(R.string.feature_rotate)) {
						Matrix matrix = new Matrix();
						int random = r.nextInt(10);
						if (random > 8) {
							matrix.postRotate(90 * r.nextInt(4));
						}
						Bitmap temp = Bitmap.createBitmap(
								icons[gameModel.getNode(x, y)], 0, 0, iconSize,
								iconSize, matrix, true);
						canvas.drawBitmap(temp, p.x, p.y, null);
					} else {
						canvas.drawBitmap(icons[gameModel.getNode(x, y)], p.x,
								p.y, null);
					}
				}
			}
		}

		if (selected != null && gameModel.getNode(selected.x, selected.y) >= 1) {
			Point p = indextoScreen(selected.x, selected.y);
			canvas.drawBitmap(selectBox,
					null, new Rect(p.x, p.y, p.x + iconSize, p.y
							+ iconSize), null);
		}
	}

	public Point indextoScreen(int x, int y) {
		return new Point(x * iconSize, y * iconSize);
	}

	public Point screenToindex(int x, int y) {
		int ix = x / iconSize;
		int iy = y / iconSize;
		if (ix < gameModel.getHeight() && iy < gameModel.getWidth()) {
			return new Point(ix, iy);
		} else {
			return new Point(0, 0);
		}
	}
}

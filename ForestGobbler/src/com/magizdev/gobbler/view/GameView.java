package com.magizdev.gobbler.view;

import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Mesh.Primitive;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.magizdev.gobbler.R;
import com.magizdev.gobbler.SoundPlay;
import com.magizdev.gobbler.view.GameModel.Node;

public class GameView extends BoardView {

	private static final int REFRESH_VIEW = 1;

	public static final int WIN = 1;
	public static final int LOSE = 2;
	public static final int PAUSE = 3;
	public static final int PLAY = 4;
	public static final int QUIT = 5;

	public static final int EASY_MODE = 10;
	public static final int HARD_MODE = 11;
	public static final int ENDLESS_MODE = 12;

	private int Help = 3;
	private int Refresh = 3;
	private int totalTime = 100;
	private int leftTime;
	private int gameMode;

	public static SoundPlay soundPlay;
	public MediaPlayer player;

	private Thread refreshTime;
	private RefreshHandler refreshHandler = new RefreshHandler();
	private boolean isStop;
	private int state;

	private OnTimerListener timerListener = null;
	private OnStateListener stateListener = null;
	private OnToolsChangeListener toolsChangedListener = null;
	private OnScoreListener scoreListener = null;

	private Handler uiHandler = new Handler() {
		@Override
		public void handleMessage(Message what) {
			invalidate();
		}
	};

	public GameView(Context context, AttributeSet atts) {
		super(context, atts);
		player = MediaPlayer.create(context, R.raw.back2new);
		player.setLooping(true);
	}

	public static final int ID_SOUND_CHOOSE = 0;
	public static final int ID_SOUND_DISAPEAR = 1;
	public static final int ID_SOUND_WIN = 4;
	public static final int ID_SOUND_LOSE = 5;
	public static final int ID_SOUND_REFRESH = 6;
	public static final int ID_SOUND_TIP = 7;
	public static final int ID_SOUND_ERROR = 8;

	public void startPlay(int gameMode) {
		this.gameMode = gameMode;
		switch (gameMode) {
		case EASY_MODE:
		case ENDLESS_MODE:
			setBoardSize(8, 9);
			break;
		case HARD_MODE:
			setBoardSize(10, 11);
			break;
		default:
			setBoardSize(9, 10);
			break;
		}
		startPlay();
	}

	public int getGameMode() {
		return gameMode;
	}

	private void startPlay() {
		Help = 3;
		Refresh = 3;
		isStop = false;
		toolsChangedListener.onRefreshChanged(Refresh);
		toolsChangedListener.onTipChanged(Help);

		leftTime = totalTime;

		if (player.isPlaying() == false) {
			player.start();
		}

		if (this.gameMode == ENDLESS_MODE) {
			refreshTime = new EndlessRefreshTime();
		} else {
			refreshTime = new RefreshTime();
		}
		refreshTime.start();
		GameView.this.invalidate();
	}

	public static void initSound(Context context) {
		soundPlay = new SoundPlay();
		soundPlay.initSounds(context);
		soundPlay.loadSfx(context, R.raw.choose, ID_SOUND_CHOOSE);
		soundPlay.loadSfx(context, R.raw.disappear1, ID_SOUND_DISAPEAR);
		soundPlay.loadSfx(context, R.raw.win, ID_SOUND_WIN);
		soundPlay.loadSfx(context, R.raw.lose, ID_SOUND_LOSE);
		soundPlay.loadSfx(context, R.raw.item1, ID_SOUND_REFRESH);
		soundPlay.loadSfx(context, R.raw.item2, ID_SOUND_TIP);
		soundPlay.loadSfx(context, R.raw.alarm, ID_SOUND_ERROR);
	}

	public void setOnTimerListener(OnTimerListener timerListener) {
		this.timerListener = timerListener;
	}

	public void setOnStateListener(OnStateListener stateListener) {
		this.stateListener = stateListener;
	}

	public void setOnToolsChangedListener(
			OnToolsChangeListener toolsChangedListener) {
		this.toolsChangedListener = toolsChangedListener;
	}

	public void setOnScoreListener(OnScoreListener scoreListener) {
		this.scoreListener = scoreListener;
	}

	public void stopTimer() {
		isStop = true;
	}

	public void startTimer() {
		isStop = false;
		refreshTime = new RefreshTime();
		refreshTime.start();
	}

	public boolean win() {
		return gameModel.win();
	}

	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == REFRESH_VIEW) {
				GameView.this.invalidate();
				if (gameModel.win()) {
					setMode(WIN);
					soundPlay.play(ID_SOUND_WIN, 0);
					isStop = true;
				} else if (gameModel.die()) {
					gameModel.change();
				}
			}
		}

		public void sleep(int delayTime) {
			this.removeMessages(0);
			Message message = new Message();
			message.what = REFRESH_VIEW;
			sendMessageDelayed(message, delayTime);
		}
	}

	class RefreshTime extends Thread {

		public void run() {
			while (leftTime >= 0 && !isStop) {
				timerListener.onTimer(leftTime);
				leftTime--;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (!isStop) {
				setMode(LOSE);
				soundPlay.play(ID_SOUND_LOSE, 0);
			}

		}
	}

	class EndlessRefreshTime extends Thread {
		public void run() {
			boolean die = false;
			int i = 0;
			while (!die && !isStop) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				timerListener.onTimer(i);
				if (i == 10) {
					die = gameModel.refill();
					uiHandler.sendEmptyMessage(1);
					i = 0;
				}
			}
			if (die) {
				setMode(LOSE);
				soundPlay.play(ID_SOUND_LOSE, 0);
			}
		}
	}

	public int getTotalTime() {
		return totalTime;
	}

	public int getTipNum() {
		return Help;
	}

	public int getRefreshNum() {
		return Refresh;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		Point p = screenToindex(x, y);
		if (gameModel.getNode(p.x, p.y) > 0) {
			Node node = gameModel.new Node(p.x, p.y);
			if (selected != null) {
				if (gameModel.link(selected, node)) {
					soundPlay.play(ID_SOUND_DISAPEAR, 0);
					scoreListener.OnScore();
					refreshHandler.sleep(500);
				} else {
					selected = node;
					soundPlay.play(ID_SOUND_CHOOSE, 0);
				}
			} else {
				selected = node;
				soundPlay.play(ID_SOUND_CHOOSE, 0);
			}
			invalidate();
		}
		return super.onTouchEvent(event);
	}

	public void setMode(int stateMode) {
		this.state = stateMode;
		this.stateListener.OnStateChanged(stateMode);
	}

	public int getMode() {
		return this.state;
	}

	public void autoClear() {
		if (Help == 0) {
			soundPlay.play(ID_SOUND_ERROR, 0);
		} else {
			soundPlay.play(ID_SOUND_TIP, 0);
			Help--;
			gameModel.noLivePath();
			toolsChangedListener.onTipChanged(Help);
			refreshHandler.sleep(500);
			invalidate();
		}
	}

	public void refreshChange() {
		if (Refresh == 0) {
			soundPlay.play(ID_SOUND_ERROR, 0);
			return;
		} else {
			soundPlay.play(ID_SOUND_REFRESH, 0);
			Refresh--;
			toolsChangedListener.onRefreshChanged(Refresh);
			gameModel.change();
			invalidate();
		}
	}

	@Override
	public boolean isInEditMode() {
		return true;
	}
}

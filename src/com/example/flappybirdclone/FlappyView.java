package com.example.flappybirdclone;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

public class FlappyView extends View {

	private static final Paint bg = new Paint();
	private static final Paint text = new Paint();
	Birdie birdie;
	public float g;
	public State state = State.OUT_TUBE;
	private List<Tube> tubes = new LinkedList<Tube>();
	public float V = 50;
	public Tube currentTube = null;
	public int score = 0;

	public FlappyView(Context context) {
		super(context);
		bg.setColor(0xff75c1ff);
		text.setColor(0xffff0000);
		text.setTextAlign(Align.CENTER);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// +нарисовать фон
		canvas.drawRect(0, 0, getWidth(), getHeight(), bg);
		// +нарисовать птичку
		birdie.draw(canvas);
		// +нарисовать трубы
		for (Tube t : tubes)
			t.draw(canvas);
		// +если проигрыш : написать об этом
		if (state == State.LOSE)
			canvas.drawText("Score: " + score, getWidth() / 2, getHeight() / 2,
					text);
		// +обновить
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		score = 0;
		g = h / 500.0f;
		text.setTextSize(w / 6);
		Bitmap bird = BitmapFactory.decodeResource(
				getResources(),
				R.drawable.ic_launcher);
		birdie = new Birdie(w / 5.0f, h / 2.0f, 0, 0, bird);
		new MyTimer(1000000000000L, 30).start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (state == State.LOSE) {
				restart();
			} else
				birdie.vy -= 100;
		}

		return true;
	}

	private void restart() {
		state = State.OUT_TUBE;
		tubes = new LinkedList<Tube>();
		birdie.y = getHeight()/2;
		birdie.vy = 0;
		score = 0;
		new MyTimer(1000000000000L, 30).start();
	}

	class MyTimer extends CountDownTimer {
		int k = 0;

		public MyTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// +изменить скорость птички
			birdie.vy += g;
			// +поменять координаты птички
			birdie.y += birdie.vy;
			// +проверить на столкновение с полом
			if (birdie.y + birdie.bird.getHeight() > FlappyView.this
					.getHeight()) {
				state = State.LOSE;
				cancel();
			}
			// +изменить координаты труб
			for (Tube t : tubes) {
				t.left -= V;
				t.right -= V;
				if (state == State.OUT_TUBE)
					if (birdie.x > t.left
							&& birdie.x + birdie.bird.getWidth() < t.right) {
						state = State.IN_TUBE;
						currentTube = t;
					}
			}
			// проверить на столкновение
			if (state == State.IN_TUBE) {
				if (birdie.y < currentTube.top
						|| birdie.y + birdie.bird.getHeight() > currentTube.bottom) {
					state = State.LOSE;
					cancel();
				}
				if (birdie.x > currentTube.right) {
					state = State.OUT_TUBE;
					score++;
				}
			}

			// +создать трубу если нужно
			if (k >= 50) {
				float r1 = (float) (Math.random() * getHeight() / 2 + getHeight() / 10);
				float r2 = (float) (Math.random() * getHeight() * 3 / 4);
				tubes.add(new Tube(getWidth(), getWidth() + getWidth() / 20,
						r2, r2 + r1));
				k = 0;
			}
			k++;
			// +удалить трубу если вышла за край экрана
			for (Iterator<Tube> iterator = tubes.iterator(); iterator.hasNext();) {
				Tube t = iterator.next();
				if (t.right < 0)
					iterator.remove();
			}

		}

		@Override
		public void onFinish() {
			start();

		}

	}

}

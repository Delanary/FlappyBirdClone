package com.example.flappybirdclone;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Tube {
	private static final Paint paint = new Paint();
	float left, right, top, bottom;
	{
		// При загрузке программы сделать:
		paint.setColor(0xff000000);
	}
	public Tube(float left, float right, float top, float bottom) {
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	public void draw(Canvas canvas) {
		canvas.drawRect(left, 0, right, top, paint);
		canvas.drawRect(left, bottom, right, canvas.getHeight(), paint);
	}
	
}

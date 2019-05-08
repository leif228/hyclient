package com.hangyi.zd.widge;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class DragLinearLayout extends LinearLayout {

	private float mLastTouchX;
	private float mLastTouchY;

	public DragLinearLayout(Context context) {
		super(context);
	}

	public DragLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// The ‘active pointer’ is the one currently moving our object.
	private int INVALID_POINTER_ID = -1000;
	private int mActivePointerId = INVALID_POINTER_ID;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		// 为了使手指按在Button等可点击的控件上任可以滑动，需要拦截滑动实践
		// 并且为了使坐标准确，在此处记录按下的点
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			return true;
		case MotionEvent.ACTION_DOWN:
			final int pointerIndex = MotionEventCompat.getActionIndex(event);

			mActivePointerId = MotionEventCompat.getPointerId(event,
					pointerIndex);

			final float x = MotionEventCompat.getX(event, pointerIndex);
			final float y = MotionEventCompat.getY(event, pointerIndex);

			// Remember where we started (for dragging)
			mLastTouchX = x;
			mLastTouchY = y;
			// Save the ID of this pointer (for dragging)
			mActivePointerId = MotionEventCompat.getPointerId(event, 0);
			mActivePointerId = MotionEventCompat.getPointerId(event, 0);
			
			return false;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Let the ScaleGestureDetector inspect all events.
		// mScaleDetector.onTouchEvent(ev);

		final int action = MotionEventCompat.getActionMasked(ev);

		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			// Find the index of the active pointer and fetch its position
			final int pointerIndex = MotionEventCompat.findPointerIndex(ev,
					mActivePointerId);

			final float x = MotionEventCompat.getX(ev, pointerIndex);
			final float y = MotionEventCompat.getY(ev, pointerIndex);

			// Calculate the distance moved
			final float dx = x - mLastTouchX;
			final float dy = y - mLastTouchY;
			layout((int) (getLeft() + dx), (int) (getTop() + dy),
					(int) (getRight() + dx), (int) (getBottom() + dy));
			invalidate();

			// Remember this touch position for the next move event
			// 使用视图坐标系（相对做坐标系），不用刷新初始点
			// mLastTouchX = x;
			// mLastTouchY = y;

			break;
		}

		case MotionEvent.ACTION_UP: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {

			final int pointerIndex = MotionEventCompat.getActionIndex(ev);
			final int pointerId = MotionEventCompat.getPointerId(ev,
					pointerIndex);

			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
				mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
				mActivePointerId = MotionEventCompat.getPointerId(ev,
						newPointerIndex);
			}
			break;
		}
		}
		return false;
	}
}
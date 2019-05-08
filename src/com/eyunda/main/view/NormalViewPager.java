package com.eyunda.main.view;

import com.eyunda.third.activities.PageHomeActivity;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class NormalViewPager extends android.support.v4.view.ViewPager {

	private Activity mGestureDetector;
	public NormalViewPager(Context arg0) {
		super(arg0);

	}

	public NormalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		PageHomeActivity.FLAG = false;
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onInterceptTouchEvent(ev);

	}
	@Override
	public boolean onTouchEvent(MotionEvent motionevent) {
		// TODO Auto-generated method stub
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(motionevent);
	}
	
	

}

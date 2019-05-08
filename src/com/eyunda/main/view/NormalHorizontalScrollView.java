package com.eyunda.main.view;

import com.eyunda.third.activities.PageHomeActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class NormalHorizontalScrollView extends HorizontalScrollView{
	public NormalHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NormalHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NormalHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		PageHomeActivity.flag=-1;
//		return false;
		 PageHomeActivity.FLAG=false;
		return super.onInterceptTouchEvent(ev);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		return super.onTouchEvent(ev);
	}
}

package com.eyunda.main.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.eyunda.main.view.callback.SizeCallBack;
import com.eyunda.third.activities.PageHomeActivity;

public class MenuHorizontalScrollView extends HorizontalScrollView {

	/* 当前控件 */
	private MenuHorizontalScrollView me;

	private LinearLayout menu;

	/* 菜单状态 */
	public static boolean menuOut;

	/* 扩展宽度 */
	private final int ENLARGE_WIDTH = 80;

	/* 菜单的宽度 */
	private int menuWidth;

	/* 手势动作最开始时的x坐标 */
	private float lastMotionX = -1;

	/* 按钮 */
	private Button menuBtn;

	/* 当前滑动的位置 */
	private int current;

	private int scrollToViewPos;


	public MenuHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
		
	}

	public MenuHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public MenuHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		this.setHorizontalFadingEdgeEnabled(false);
		this.setVerticalFadingEdgeEnabled(false);
		this.me = this;
		this.me.setVisibility(View.INVISIBLE);
		menuOut = false;
		
	}
	public void initViews(View[] children, SizeCallBack sizeCallBack,
			LinearLayout menu) {
		this.menu = menu;
		ViewGroup parent = (ViewGroup) getChildAt(0);

		for (int i = 0; i < children.length; i++) {
			children[i].setVisibility(View.INVISIBLE);
			parent.addView(children[i]);
		}

		OnGlobalLayoutListener onGlLayoutistener = new MenuOnGlobalLayoutListener(
				parent, children, sizeCallBack);
		getViewTreeObserver().addOnGlobalLayoutListener(onGlLayoutistener);

	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN){
			PageHomeActivity.FLAG = true;
			
			return super.onInterceptTouchEvent(ev);
		}else if(ev.getAction() == MotionEvent.ACTION_MOVE)
			return false;
		else
			return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 设置按钮
	 * 
	 * @param btn
	 */
	public void setMenuBtn(Button btn) {
		this.menuBtn = btn;
	}

	public void clickMenuBtn() {

		if (!menuOut) {
			this.menuWidth = 0;
		} else {
			this.menuWidth = this.menu.getMeasuredWidth()
					- this.menuBtn.getMeasuredWidth();
		}
		menuSlide();
	}

	/**
	 * 滑动出菜单
	 */
	private void menuSlide() {

		if (this.menuWidth == 0) {
			menuOut = true;
		} else {
			menuOut = false;
			this.menuWidth = this.menu.getMeasuredWidth()
					- this.menuBtn.getMeasuredWidth();
		}
		me.smoothScrollTo(this.menuWidth, 0);

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if (l < (this.menu.getMeasuredWidth() - this.menuBtn.getMeasuredWidth() - this.ENLARGE_WIDTH) / 2) {
			this.menuWidth = 0;
		} else {
			this.menuWidth = this.menu.getWidth()
					- this.menuBtn.getMeasuredWidth() - this.ENLARGE_WIDTH;
		}
		this.current = l;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// return false;
		if( !PageHomeActivity.FLAG )
			return true;
		int x = (int) ev.getRawX();

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			/* 手指按下时的x坐标 */
			this.lastMotionX = (int) ev.getRawX();
		}
		if ((this.current == 0 && x < this.scrollToViewPos)
				|| (this.current == this.scrollToViewPos * 2 && x > this.ENLARGE_WIDTH)) {
			return false;
		}
		if (menuOut == false && this.lastMotionX > 20) {
			// return true;
			if (ev.getAction() == MotionEvent.ACTION_UP) {
				menuSlide();
				return false;
			}
		}

		else {
			if (ev.getAction() == MotionEvent.ACTION_UP) {
				menuSlide();
				return false;
			}
		}
		return super.onTouchEvent(ev);
	}

	/****************************************************/
	/*-												   -*/
	/*-			Class 			Area				   -*/
	/*-												   -*/
	/****************************************************/

	public class MenuOnGlobalLayoutListener implements OnGlobalLayoutListener {

		private ViewGroup parent;
		private View[] children;
		// private int scrollToViewIndex = 0;
		private SizeCallBack sizeCallBack;

		public MenuOnGlobalLayoutListener(ViewGroup parent, View[] children,
				SizeCallBack sizeCallBack) {

			this.parent = parent;
			this.children = children;
			this.sizeCallBack = sizeCallBack;
		}

		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			me.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			this.sizeCallBack.onGlobalLayout();
			this.parent.removeViewsInLayout(0, children.length);
			int width = me.getMeasuredWidth();
			int height = me.getMeasuredHeight();

			int[] dims = new int[2];
			scrollToViewPos = 0;

			for (int i = 0; i < children.length; i++) {
				this.sizeCallBack.getViewSize(i, width, height, dims);
				children[i].setVisibility(View.VISIBLE);

				parent.addView(children[i], dims[0], dims[1]);
				if (i == 0) {
					scrollToViewPos += dims[0];
				}
			}
			// if(firstLoad){
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					me.scrollBy(scrollToViewPos, 0);

					/* 视图不是中间视图 */
					me.setVisibility(View.VISIBLE);
					menu.setVisibility(View.VISIBLE);
				}
			});
			// }

		}
	}

}

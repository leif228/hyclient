package com.eyunda.tools;

import java.util.Date;

import com.ygl.android.view.listview.OnRefreshListener;
import com.ygl.android.view.listview.a;
import com.ygl.android.view.listview.b;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GQListView extends ListView implements OnScrollListener {
	public static boolean LOADFLAG = false;
	int a;
	Context context;
	int visibleLastIndex;//最后的可视项索引 c
	private LayoutInflater d;
	private LinearLayout e;
	private TextView f;//下拉组件文本内容
	private TextView g;//下拉组件文本
	private ImageView h;//下拉组件箭头向上
	private ImageView i;//下拉组件箭头向下
	private RotateAnimation j;
	private RotateAnimation k;
	private boolean l;
	public boolean load;
	private int m;
	private int n;
	private int o;
	private int p;
	private int visibleItemCount;// 当前窗口可见项总数q
	private boolean r;
	private OnRefreshListener onRefresh;//s
	public boolean showHead;
	private boolean t;
	public GQListView(Context context) {
		super(context);
	}

	public GQListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		showHead = false;
		load = true;
		visibleLastIndex = 0;
		this.context = context;
	}

	public GQListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		ListAdapter listadapter = getAdapter();
		if (visibleLastIndex == (listadapter.getCount() - 1) - a && scrollState == 0 && onRefresh != null)
			onRefresh.addDate();//更新数据
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount= visibleItemCount;
		visibleLastIndex = (firstVisibleItem + visibleItemCount)-2;

	}
	
	private void a()//下拉状态
	{
		if (showHead){
			switch (visibleItemCount)
			{
			case 0:
				h.setVisibility(0);
				i.setVisibility(8);
				f.setVisibility(0);
				g.setVisibility(0);
				h.clearAnimation();
				h.startAnimation(j);
				f.setText("松开刷新");
				break;

			case 1: 
				i.setVisibility(8);
				f.setVisibility(0);
				g.setVisibility(0);
				h.clearAnimation();
				h.setVisibility(0);
				if (r)
				{
					r = false;
					h.clearAnimation();
					h.startAnimation(k);
					f.setText("下拉刷新");
				} else
				{
					f.setText("下拉刷新");
				}
				break;

			case 2: 
				e.setPadding(0, 0, 0, 0);
				i.setVisibility(0);
				h.clearAnimation();
				h.setVisibility(8);
				f.setText("正在刷新...");
				g.setVisibility(0);
				break;

			case 3:
				e.setPadding(0, -1 * n, 0, 0);
				i.setVisibility(8);
				h.clearAnimation();
				h.setImageBitmap(com.ygl.android.view.listview.a.a());
				f.setText("下拉刷新");
				g.setVisibility(0);
				break;
			}
		}else{
			
		}
	}
	
	public void addFooterView(View view)
	{
		super.addFooterView(view);
		a = 1 + a;
	}

	public void init(Context context)
	{
		a = 0;
		setCacheColorHint(0);
		d = LayoutInflater.from(context);
		e = new b(context);
		h = ((b)e).c();
		i = ((b)e).d();
		f = ((b)e).a();
		g = ((b)e).b();
		h.setImageBitmap(com.ygl.android.view.listview.a.a());
		LinearLayout linearlayout = e;
		android.view.ViewGroup.LayoutParams layoutparams = linearlayout.getLayoutParams();
		if (layoutparams == null)
			layoutparams = new android.view.ViewGroup.LayoutParams(-1, -2);
		int i1 = ViewGroup.getChildMeasureSpec(0, 0, layoutparams.width);
		int j1 = layoutparams.height;
		int k1;
		if (j1 > 0)
			k1 = android.view.View.MeasureSpec.makeMeasureSpec(j1, 0x40000000);
		else
			k1 = android.view.View.MeasureSpec.makeMeasureSpec(0, 0);
		linearlayout.measure(i1, k1);
		n = e.getMeasuredHeight();
		m = e.getMeasuredWidth();
		e.setPadding(0, -1 * n, 0, 0);
		e.invalidate();
		if (showHead)
			addHeaderView(e, null, false);
		setOnScrollListener(this);
		j = new RotateAnimation(0.0F, -180F, 1, 0.5F, 1, 0.5F);
		j.setInterpolator(new LinearInterpolator());
		j.setDuration(250L);
		j.setFillAfter(true);
		k = new RotateAnimation(-180F, 0.0F, 1, 0.5F, 1, 0.5F);
		k.setInterpolator(new LinearInterpolator());
		k.setDuration(200L);
		k.setFillAfter(true);
		visibleItemCount = 3;
		t = false;
	}

	public void onRefreshComplete()
	{
		visibleItemCount = 3;
		g.setText((new StringBuilder("最近更新:")).append((new Date()).toLocaleString()).toString());
		a();
	}
	public boolean removeFooterView(View view)
	{
		boolean flag = super.removeFooterView(view);
		if (flag)
			a = -1 + a;
		return flag;
	}

	public void setAdapter(BaseAdapter baseadapter)
	{
		g.setText((new StringBuilder("最近更新:")).append((new Date()).toLocaleString()).toString());
		super.setAdapter(baseadapter);
	}

	public void setonRefreshListener(OnRefreshListener onrefreshlistener)
	{
		onRefresh = onrefreshlistener;
		t = true;
	}
	public boolean onTouchEvent(MotionEvent motionevent)
	{
		motionevent.getAction();
		if (p == 0 && !l)
		{
			l = true;
			o = (int)motionevent.getY();
		}
		if (visibleItemCount != 2 && visibleItemCount != 4)
		{
			if (visibleItemCount == 1)
			{
				visibleItemCount = 3;
				a();
			}
			if (visibleItemCount == 0)
			{
				visibleItemCount = 2;
				a();
				if (onRefresh != null)onRefresh.onRefresh();
			}
		}
		l = false;
		r = false;
		int i1;
		i1 = (int)motionevent.getY();
		if (!l && p == 0)
		{
			l = true;
			o = i1;
		}
//		if (visibleItemCount == 2 || !l || visibleItemCount == 4);
		if (visibleItemCount == 0)
		{
			setSelection(0);
			if ((i1 - o) / 3 < n && i1 - o > 0)
			{
				visibleItemCount = 1;
				a();
			} else
			if (i1 - o <= 0)
			{
				visibleItemCount = 3;
				a();
			}
	     }
		if (visibleItemCount == 1)
		{
			setSelection(0);
			//if ((i1 - o) / 3 < n)break; 
			visibleItemCount = 0;
			r = true;
			a();
		}
		if (visibleItemCount == 3 && i1 - o > 0)
		{
			visibleItemCount = 1;
			a();
		}
		if (visibleItemCount == 1)
			e.setPadding(0, -1 * n + (i1 - o) / 3, 0, 0);
		if (visibleItemCount == 0)
			e.setPadding(0, (i1 - o) / 3 - n, 0, 0);
		
		return super.onTouchEvent(motionevent);
	}
}

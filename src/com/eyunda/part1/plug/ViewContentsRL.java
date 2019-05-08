package com.eyunda.part1.plug;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 标题栏
 * 点击view
 * 头像
 * 标题
 * 内容
 * 是否关注
 * 推荐图标
 * 可点击的条目
 * view容器
 * @author ygl-pc
 *
 */
public class ViewContentsRL {
	private RelativeLayout bigTitle_click;
	private TextView  bigTitle;
	private  TextView  month;
	private TextView[] titles;
	private  TextView[] contents;
	
	private View[] clickItems;
	private LinearLayout djs;
	public LinearLayout getDjs() {
		return djs;
	}

	public void setDjs(LinearLayout djs) {
		this.djs = djs;
	}

	public View[] getClickItems() {
		return clickItems;
	}

	public void setClickItems(View[] clickItems) {
		this.clickItems = clickItems;
	}

	private  TextView  day;
	private  TextView  daydis;
	private  TextView  dis;
	 
	private LinearLayout root;
	
	public LinearLayout getRoot() {
		return root;
	}

	public void setRoot(LinearLayout root) {
		this.root = root;
	}
 



	public RelativeLayout getBigTitle_click() {
		return bigTitle_click;
	}

	public void setBigTitle_click(RelativeLayout bigTitle_click) {
		this.bigTitle_click = bigTitle_click;
	}

	public TextView getBigTitle() {
		return bigTitle;
	}

	public void setBigTitle(TextView bigTitle) {
		this.bigTitle = bigTitle;
	}

	public TextView[] getTitles() {
		return titles;
	}

	public void setTitles(TextView[] titles) {
		this.titles = titles;
	}

	public TextView[] getContents() {
		return contents;
	}

	public void setContents(TextView[] contents) {
		this.contents = contents;
	}

	public TextView getMonth() {
		return month;
	}

	public void setMonth(TextView month) {
		this.month = month;
	}

	public TextView getDay() {
		return day;
	}

	public void setDay(TextView day) {
		this.day = day;
	}

	public TextView getDaydis() {
		return daydis;
	}

	public void setDaydis(TextView daydis) {
		this.daydis = daydis;
	}

	public TextView getDis() {
		return dis;
	}

	public void setDis(TextView dis) {
		this.dis = dis;
	}
 

}

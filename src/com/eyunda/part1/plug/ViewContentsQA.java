package com.eyunda.part1.plug;

import android.view.View;
import android.widget.ImageView;
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
public class ViewContentsQA {
	private RelativeLayout bigTitle_click;
	private TextView  bigTitle;
	private TextView[] titles;
	private  TextView[] contents;
	private TextView[] favors;
 private TextView[] times;
	private View[] clickItems;
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

	public TextView[] getFavors() {
		return favors;
	}

	public void setFavors(TextView[] favors) {
		this.favors = favors;
	}

 

	public TextView[] getTimes() {
		return times;
	}

	public void setTimes(TextView[] times) {
		this.times = times;
	}

	public View[] getClickItems() {
		return clickItems;
	}

	public void setClickItems(View[] clickItems) {
		this.clickItems = clickItems;
	}

}

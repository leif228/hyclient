package com.eyunda.part1.plug;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 标题栏
 * 点击view
 * 图标
 * 标题
 * 描述
 * 载货量
 * 航线
 * 报价
 * 是否关注
 * 推荐图标
 * 可点击的条目
 * view容器
 * @author ygl-pc
 *
 */
public class ViewContents {
	private RelativeLayout bigTitle_click;
	private TextView  bigTitle;//热门航线
	
	private TextView[] tons;//MMSI
	private  TextView[] ways;//航线
	private  TextView[] prices;//总价
	private  TextView[] times;//截止时间
	private  TextView[] ones;//申请接货
	private  TextView[] zaihuos;//载货量
	private ImageView[] favors;
	private ImageView[] logos;
	private ImageView[] jians;
	private View[] clickItems;
	private LinearLayout root;
	private ImageView arrow;
	private TextView[] titles;//船舶名称
	private  TextView[] contents;//航区
	public TextView[] getTons() {
		return tons;
	}

	public void setTons(TextView[] tons) {
		this.tons = tons;
	}

	public TextView[] getWays() {
		return ways;
	}

	public void setWays(TextView[] ways) {
		this.ways = ways;
	}

	public TextView[] getPrices() {
		return prices;
	}

	public void setPrices(TextView[] prices) {
		this.prices = prices;
	}


	
	public ImageView getArrow() {
		return arrow;
	}

	public void setArrow(ImageView arrow) {
		this.arrow = arrow;
	}

	public LinearLayout getRoot() {
		return root;
	}

	public void setRoot(LinearLayout root) {
		this.root = root;
	}

	public ImageView[] getJians() {
		return jians;
	}

	public void setJians(ImageView[] jians) {
		this.jians = jians;
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

	public ImageView[] getFavors() {
		return favors;
	}

	public void setFavors(ImageView[] favors) {
		this.favors = favors;
	}

	public TextView[] getTimes() {
		return times;
	}

	public void setTimes(TextView[] times) {
		this.times = times;
	}

	public TextView[] getZaihuos() {
		return zaihuos;
	}

	public void setZaihuos(TextView[] zaihuos) {
		this.zaihuos = zaihuos;
	}

	public ImageView[] getLogos() {
		return logos;
	}

	public void setLogos(ImageView[] logos) {
		this.logos = logos;
	}

	public View[] getClickItems() {
		return clickItems;
	}

	public void setClickItems(View[] clickItems) {
		this.clickItems = clickItems;
	}

	public TextView[] getOnes() {
		return ones;
	}

	public void setOnes(TextView[] ones) {
		this.ones = ones;
	}



}

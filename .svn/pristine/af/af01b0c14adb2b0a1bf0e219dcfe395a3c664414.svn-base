package com.eyunda.third.adapters.home;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.eyunda.third.activities.notice.NoticeListActivity;
import com.eyunda.third.domain.notice.NoticeData;
import com.hy.client.R;

public class JDViewAdapter {
	private List<NoticeData> mDatas;
	private Context context;

	public JDViewAdapter(Context context, List<NoticeData> anDatas) {
		this.mDatas = anDatas;
		this.context = context;
		if (anDatas == null || anDatas.isEmpty()) {
//			throw new RuntimeException("没有要展示的数据！");
		}
	}
	
	public void refrushDatas(List<NoticeData> anDatas) {
		this.mDatas = anDatas;
	}

	/**
	 * 获取数据的条数
	 * 
	 * @return
	 */
	public int getCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	/**
	 * 获取摸个数据
	 * 
	 * @param position
	 * @return
	 */
	public NoticeData getItem(int position) {
		if (mDatas == null || mDatas.isEmpty())
			return null;
		return mDatas.get(position);
	}

	/**
	 * 获取条目布局
	 * 
	 * @param parent
	 * @return
	 */
	public View getView(JDAdverView parent) {
		return LayoutInflater.from(parent.getContext()).inflate(
				R.layout.jd_adver_view, null);
	}

	/**
	 * 条目数据适配
	 * 
	 * @param view
	 * @param data
	 */
	public void setItem(final View view, final NoticeData data) {
		if(data == null)
			return;
		TextView tv = (TextView) view.findViewById(R.id.title);
		tv.setText(data.getTitle());
		TextView tag = (TextView) view.findViewById(R.id.tag);
		tag.setText(data.getTitle());
		// 你可以增加点击事件
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 比如打开url
				context.startActivity(new Intent(context, NoticeListActivity.class));
			}
		});
	}
}

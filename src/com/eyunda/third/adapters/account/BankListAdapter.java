package com.eyunda.third.adapters.account;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.account.BankListActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.SpinnerItem;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BankListAdapter extends ArrayAdapter<SpinnerItem>{
	
	private int res;
	private LayoutInflater layoutInflater;
	ImageLoader mImageLoader;
	private String flag;

	public BankListAdapter(Context context, int resource, List<SpinnerItem> list, String flag) {
		super(context, resource, list);
		this.res = resource;
		this.flag = flag;
		this.layoutInflater = LayoutInflater.from(context);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance()
				.getImageLoaderConfiguration());
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = layoutInflater.inflate(res, null);
		}

		ImageView avatar = (ImageView) convertView
				.findViewById(R.id.avatar);
		TextView nameTextview = (TextView) convertView
				.findViewById(R.id.name);
		TextView signature = (TextView) convertView
				.findViewById(R.id.signature);
		
		SpinnerItem s = getItem(position);
		
		mImageLoader.displayImage(
				ApplicationConstants.SERVER_URL + s.getValue(),
				avatar, MyshipAdapter.displayImageOptions);
		
		if(BankListActivity.BINDED_BANK_CARD.equals(flag)){
			signature.setVisibility(View.VISIBLE);
			signature.setText(s.getFlag());
		}else
			signature.setVisibility(View.GONE);
		
		nameTextview.setText(s.getData());
		
		return convertView;

	}
}

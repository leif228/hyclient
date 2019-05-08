package com.eyunda.third.adapters.user;

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
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.account.AccountData;
import com.eyunda.third.domain.account.UserData;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AccountAdapter extends ArrayAdapter<AccountData>{
	
	private int res;
	private LayoutInflater layoutInflater;
	ImageLoader mImageLoader;

	public AccountAdapter(Context context, int resource, List<AccountData> list) {
		super(context, resource, list);
		this.res = resource;
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
		
		avatar.setVisibility(View.INVISIBLE);
		AccountData user = getItem(position);
		nameTextview.setText(user.getAccounter());
		signature.setText(user.getAccountNo());
		
		return convertView;

	}
}

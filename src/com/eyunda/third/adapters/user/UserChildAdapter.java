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
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserChildAdapter extends ArrayAdapter<UserData>{
	
	private int res;
	private LayoutInflater layoutInflater;
	ImageLoader mImageLoader;

	public UserChildAdapter(Context context, int resource, List<UserData> list) {
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
		UserData user = getItem(position);
		mImageLoader.displayImage(
				ApplicationConstants.IMAGE_URL + user.getUserLogo(),
				avatar, MyshipAdapter.displayImageOptions);
		String username = user.getNickName();
		if("".equals(username))
			username = user.getTrueName();
		if("".equals(username))
			username = user.getLoginName();
		
		nameTextview.setText(username);
		if(!user.getShips().equals("")){
			signature.setText(UserRoleCode.sailor.getDescription());
		}else{
			signature.setText(UserRoleCode.handler.getDescription());
		}
//		signature.setVisibility(View.INVISIBLE);
		
		return convertView;

	}
}

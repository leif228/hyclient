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
import com.eyunda.third.domain.ship.ShipNameData;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SpinnerAdapter extends ArrayAdapter<ShipNameData>{
	
	private int res;
	private LayoutInflater layoutInflater;

	public SpinnerAdapter(Context context, int resource,int s, List<ShipNameData> list) {
		super(context, resource,s, list);
		this.res = resource;
		this.layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.spinner_item, null);
		}

		TextView nameTextview = (TextView) convertView
				.findViewById( R.id.contentTextView);
		
		ShipNameData user = getItem(position);
		nameTextview.setText(user.getShipName());
		
		return convertView;

	}
}

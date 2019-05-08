package com.eyunda.third.activities.map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.loaders.Image_loader;
import com.eyunda.tools.log.Log;
import com.hy.client.R;

/**
 * 船舶分布
 * 
 * @author guoqiang
 *
 */
public class ShipPicActivity extends CommonActivity{

	Image_loader imgLoader;
	String pic;
	ImageView shipPic;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_ship_pic);
		imgLoader = new Image_loader(this, (GlobalApplication) getApplication());
		shipPic = (ImageView) findViewById(R.id.shipPic);
		Intent intent = getIntent();
		pic = intent.getStringExtra("pic");
		
	}


	@Override
	protected void onStart() {
		super.onStart();
		String headString = ApplicationConstants.SERVER_URL+pic;
		Log.v(headString);
		imgLoader.load_horizontal_Img(headString, shipPic);
		setTitle("查看图片");
	}




}

package com.eyunda.main.reg;

import android.os.Bundle;
import android.webkit.WebView;

import com.eyunda.main.CommonActivity;
import com.hy.client.R;

public class Detail extends CommonActivity{
	String title;
	WebView wv1;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.webdetail);
		title=getIntent().getStringExtra("title");
		wv1=(WebView) findViewById(R.id.wv1);
		if(title.equals("隐私政策"))
		wv1.loadUrl("http://115.28.227.82/hbksy/privacy.html");
		else wv1.loadUrl("http://115.28.227.82/hbksy/license.html");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		setTitle(title);
	}
	
	

}

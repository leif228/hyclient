package com.eyunda.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.eyunda.main.view.DialogUtil;
import com.hy.client.R;
import com.ygl.android.ui.BaseActivity;

public class CommonActivity extends BaseActivity {
	protected Button top_back, top_commit;
	protected TextView top_text, top_commit_text;
	protected DialogUtil dialogUtil;
	protected ProgressDialog dialog;
	private Button top_Search;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		dialogUtil = new DialogUtil(this);
	}
	
	@Override
	protected void onStart() {

		super.onStart();
		top_back = (Button) findViewById(R.id.top_back);
		top_text = (TextView) findViewById(R.id.top_text);
		top_commit = (Button) findViewById(R.id.top_commit);
		top_Search = (Button)findViewById(R.id.top_search);
		top_commit_text = (TextView) findViewById(R.id.top_commit_text);
		if (top_back != null)
			top_back.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
	}

	protected void setTitle(String name) {
		if (top_text != null)
			top_text.setText(name);

	}
    protected void  setRightBtn(int resource,OnClickListener listener) {
    	top_Search.setBackgroundResource(resource);
    	if (listener != null)
    		top_Search.setOnClickListener(listener);
	}
    
	protected void setRight(int resource, OnClickListener listener) {

		top_commit.setBackgroundResource(resource);
		if (listener != null)
			top_commit.setOnClickListener(listener);

	}

	protected void  setRight(String name,OnClickListener listener) {
		
		top_commit_text.setText(name);
		if(listener!=null)
		top_commit_text.setOnClickListener(listener);
		
	}
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		ad.onResume();
//	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
}

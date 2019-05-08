package com.eyunda.main;

import com.eyunda.main.view.DialogUtil;
import com.hy.client.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public abstract class CommonListActivity extends BaseListActivity{
	protected Button  top_back;
	protected Button top_commit,top_edit;
	protected TextView top_text,top_commit_text;
	protected OnClickListener listener;
	
	protected DialogUtil dialogUtil;
	protected ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}
	@Override
	protected void onStart() {
	 
		super.onStart();
		dialogUtil = new DialogUtil(this);
		top_back=(Button) findViewById(R.id.top_back);
		top_commit=(Button) findViewById(R.id.top_commit);
		top_edit=(Button) findViewById(R.id.top_edit);
		top_text=(TextView) findViewById(R.id.top_text);
		top_commit_text=(TextView) findViewById(R.id.top_commit_text);
		listener = leftListener();
		setLeft(listener);
		
	}
	protected void  setLeft(OnClickListener listener) {
		if(top_back!=null) top_back.setOnClickListener(listener);
	}
	
	protected OnClickListener leftListener(){
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		};
		return listener;
	}
	
	protected void  setTitle(String name) {
		
		top_text.setText(name);
		
	}
	
	protected void  setRight(String name,OnClickListener listener) {
		
		top_commit_text.setText(name);
		if(listener!=null)
		top_commit_text.setOnClickListener(listener);
		
	}
	protected void  setRight(int resource,OnClickListener listener) {
		
		top_commit.setBackgroundResource(resource);
		if(listener!=null)
		top_commit.setOnClickListener(listener);
		
	}
    protected void  setRightBtn(int resource,OnClickListener listener) {
    	top_edit.setBackgroundResource(resource);
    	if (listener != null)
    		top_edit.setOnClickListener(listener);
    	top_edit.setVisibility(View.VISIBLE);
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

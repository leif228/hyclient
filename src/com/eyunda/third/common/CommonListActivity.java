package com.eyunda.third.common;

import com.hy.client.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public abstract class CommonListActivity extends BaseListActivity{
	protected Button  top_back,top_commit;
	protected TextView top_text,top_commit_text;
 
	@Override
	protected void onStart() {
	 
		super.onStart();
		top_back=(Button) findViewById(R.id.top_back);
		top_commit=(Button) findViewById(R.id.top_commit);
		top_text=(TextView) findViewById(R.id.top_text);
		top_commit_text=(TextView) findViewById(R.id.top_commit_text);
		if(top_back!=null) top_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
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

}

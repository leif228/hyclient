package com.eyunda.part1.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hy.client.R;

public class SwitchLay extends LinearLayout {
Handler handler;
	Button close, open;

	private boolean state = true;

	public boolean isOPen() {

		return state;

	}

	public SwitchLay(Context context) {
		super(context);
	}

	public SwitchLay(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.switchlay, this);
		close = (Button) findViewById(R.id.close);
		open = (Button) findViewById(R.id.open);
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				state=!state;
				setState(state);
			}
		});
		open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				state=!state;
				setState(state);
			}
		});
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				state=!state;
				setState(state);
			}
		});
		
	 
		
		setState(state);
	}

	public void setState(boolean state) {
this.state=state;
		if (state) {
			close.setTextColor(0xff5d5d5d);
			close.setBackgroundColor(0x00ffffff);
			open.setTextColor(0xffffffff);
			open.setBackgroundResource(R.drawable.switch_checked);
		} else {
			open.setTextColor(0xff5d5d5d);
			open.setBackgroundColor(0x00ffffff);
			close.setTextColor(0xffffffff);
			close.setBackgroundResource(R.drawable.switch_checked);

		}
		if(handler!=null)
		{
			
			Message msg=new Message();
			msg.obj=this.getTag();
		handler.sendMessage(msg);
		}
	}
	
	public void setHandler(Handler handler){
		this.handler=handler;
	}

}

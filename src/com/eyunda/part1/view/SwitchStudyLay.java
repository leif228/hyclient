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

public class SwitchStudyLay extends LinearLayout {
	Handler handler;
	Button close, open;

	public boolean clickabel = false;
	private boolean state = true;

	public boolean isOPen() {

		return state;

	}

	public SwitchStudyLay(Context context) {
		super(context);
	}

	public SwitchStudyLay(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.switchstudylay, this);
		close = (Button) findViewById(R.id.close);
		open = (Button) findViewById(R.id.open);
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clickabel)return;
				state = !state;
				changeState(state);
			}
		});
		open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clickabel)return;
				state = !state;
				changeState(state);
			}
		});
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clickabel)return;
				state = !state;
				changeState(state);
			}
		});

		changeState(state);
	}

	private void changeState(boolean state) {
		setState(state);

		if (handler != null) {

			Message msg = new Message();
			msg.obj = this.getTag();
			msg.obj = state;
			handler.sendMessage(msg);
		}
	}

	public void setState(boolean state) {
		 
		this.state = state;
		if (state) {
			close.setTextColor(0xff5d5d5d);
			close.setBackgroundColor(0x008A8A8A);
			open.setTextColor(0xffffffff);
			open.setBackgroundResource(R.drawable.switch_checked);
		} else {
			open.setTextColor(0xff5d5d5d);
			open.setBackgroundColor(0x008A8A8A);
			close.setTextColor(0xffffffff);
			close.setBackgroundColor(0xff8A8A8A);

		}

	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}

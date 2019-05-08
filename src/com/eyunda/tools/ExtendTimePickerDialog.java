package com.eyunda.tools;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TimePicker;

public class ExtendTimePickerDialog extends TimePickerDialog {
	private int hour;
	private int minute;
	private TimePicker view;
	private OnTimeSetListener callBack = null;

	public ExtendTimePickerDialog(Context context, 
			OnTimeSetListener callBack, int hourOfDay, int minute,
			boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
		this.hour = hourOfDay;
		this.minute = minute;
		this.callBack = callBack;
		this.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onSetTime();
			}	
		});
	}
	
	private void onSetTime() {
		if(this.view == null)
			return;
		callBack.onTimeSet(this.view, this.hour, this.minute);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		super.onTimeChanged(view, hourOfDay, minute);
		this.view = view;
		this.hour = hourOfDay;
		this.minute = minute;
	}
	
	

}

package com.eyunda.tools;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

public class ExtendDatePickerDialog extends DatePickerDialog {
	private OnDateSetListener callBack = null;
	private DatePicker view = null;

	public ExtendDatePickerDialog(Context context,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		this.callBack = callBack;
		this.setButton(DialogInterface.BUTTON_POSITIVE, "选择",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				onSetDate();
			}
		});
	}
	
	private void onSetDate() {
		if(view == null)
			return;		
		callBack.onDateSet(view, view.getYear(), view.getMonth(), view.getDayOfMonth());
	}	

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
		this.view = view;
	}

}

package com.eyunda.third.activities.ship.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class DuesDatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
	private TextView tv;
	public DuesCallBack mc; 
	CustomerDatePickerDialog dd ;

	public DuesDatePickerFragment(TextView tv) {
		this.tv = tv;
	}
	 
	public   interface  DuesCallBack {  
	    void  getStartTime(Calendar c);  
	} 
	  
    public void setCallfuc(DuesCallBack mc){  
       this.mc= mc;  
    }

	// 返回日期或时间选择
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		// TimePickerDialog td = new TimePickerDialog(getActivity(), this, hour,
		// minute, true);
		dd = new CustomerDatePickerDialog(getActivity(), this, year,month, day);
		
		dd.show();
		DatePicker dp = findDatePicker((ViewGroup) dd.getWindow().getDecorView());  
		if (dp != null) {  
		    ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);  
		}  
		return dd;

	}

	// 选择日期
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Log.d("OnDateSet", year+"--"+month+"--"+day);
		String newDate = new StringBuilder().append(year).append("年")
				.append((month + 1) < 10 ? "0" + (month + 1) : (month + 1)).append("月")
				.toString();
		tv.setText(newDate);
		
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		mc.getStartTime(c);
	}

	// 选择时间
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// 处理设置的时间，这里我们作为示例，在日志中输出我们选择的时间
		Log.d("onTimeSet", "hourOfDay: " + hourOfDay + "Minute: " + minute);
		String strHourOfDay = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String times = strHourOfDay + ":" + strMinute;
		tv.setText(times);
	}

	public String getCurrentTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月",
				Locale.CHINA);
		return sDateFormat.format(new java.util.Date());
	}

	public String getNextYear() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月",
				Locale.CHINA);
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR)+1;
		int month = c.get(Calendar.MONTH)-1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		c.set(year, month, day);
		String date = sDateFormat.format(c.getTime());
		return date;
	}

	private DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}
	
    class CustomerDatePickerDialog extends DatePickerDialog {  
        public CustomerDatePickerDialog(Context context,  
                OnDateSetListener callBack, int year, int monthOfYear,  
                int dayOfMonth) {  
            super(context, callBack, year, monthOfYear, dayOfMonth);  
        }  
      
        @Override  
        public void onDateChanged(DatePicker view, int year, int month, int day) {  
            super.onDateChanged(view, year, month, day);  
            dd.setTitle(year + "年" + (month + 1) + "月");  
        }  
    }  

}
package com.eyunda.third.activities.ship.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class DatePickerFragment extends DialogFragment implements  
DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener {  
	private TextView tv;
	public DatePickerFragment(TextView tv) {
		this.tv=tv;
	}
	
	//返回日期或时间选择
	@Override  
	public Dialog onCreateDialog(Bundle savedInstanceState) {  
		final Calendar c = Calendar.getInstance();  
		int year = c.get(Calendar.YEAR);  
		int month = c.get(Calendar.MONTH);  
		int day = c.get(Calendar.DAY_OF_MONTH);  

		int hour = c.get(Calendar.HOUR_OF_DAY);  
		int minute = c.get(Calendar.MINUTE);
		//TimePickerDialog td = new TimePickerDialog(getActivity(), this, hour, minute, true); 
		DatePickerDialog dd = new DatePickerDialog(getActivity(), this, year, month, day);
		return dd;

	}  

	//选择日期
	@Override  
	public void onDateSet(DatePicker view, int year, int month, int day) {  
		//	Log.d("OnDateSet", year+"--"+month+"--"+day);  
		String newDate = new StringBuilder().append(year).append("-")
				.append((month + 1) < 10 ? "0" + (month + 1) : (month + 1)).append("-")
				.append((day < 10) ? "0" + day : day).toString();
		tv.setText(newDate);
	}
	//选择时间
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		//处理设置的时间，这里我们作为示例，在日志中输出我们选择的时间  
		Log.d("onTimeSet", "hourOfDay: "+hourOfDay + "Minute: "+minute);  
		String strHourOfDay = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay; 
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String times = strHourOfDay+":"+strMinute;
		tv.setText(times);
	}  
	public String getCurrentTime(){
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);     
		return  sDateFormat.format(new java.util.Date()); 
	}
	
   public String getLastMonth(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)-1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		c.set(year, month,day);
		String    date    =    sDateFormat.format(c.getTime());
		return date;
   }

}  
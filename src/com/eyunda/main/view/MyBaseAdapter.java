package com.eyunda.main.view;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.eyunda.main.data.Image_loader;
import com.ta.TAApplication;

/**
 * 
 * @author user_ygl
 * 
 */
public abstract class MyBaseAdapter extends SimpleAdapter {
	Image_loader loader;
	private List<? extends Map<String, ?>> data;
	private Context context;

	/**
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 *            xm文件
	 * @param from
	 *            对应的id
	 * @param to
	 *            对应的data数据
	 */
	public MyBaseAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.context=context;
		this.data = data;
		loader=new Image_loader(context, (TAApplication) context.getApplicationContext());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		iniview(view, position, data);
		return view;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	public abstract void iniview(View view, int position,
			List<? extends Map<String, ?>> data);

	public void setViewBinder() {
		super.setViewBinder(new MyViewBinder());
	}

	public class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			if (data != null && (view instanceof ImageView)
					& (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			if (data != null && (view instanceof ImageView)
					& (data instanceof Integer)) {
				ImageView iv = (ImageView) view;
				Integer bm = (Integer) data;
				iv.setImageResource(bm);
				return true;
			}
			if (data != null && (view instanceof ImageView)
					& (data instanceof String)) {
				ImageView iv = (ImageView) view;
				String bm = (String) data;
				
				loader.load_normal_Img(bm,iv);
//				iv.setBackgroundResource(bm);
				return true;
			}
			  if (data != null && (view instanceof TextView)){
				((TextView) view).setText(Html.fromHtml((String) data) );
			return true;
			}
			return false;
		}
	}
}

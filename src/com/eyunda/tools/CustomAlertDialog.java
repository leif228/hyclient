package com.eyunda.tools;


import com.hy.client.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CustomAlertDialog extends Dialog {
	public CustomAlertDialog(Context c, int theme) {
		super(c, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private ListAdapter adapter;
		private ListView listView;
		private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;
		private	OnItemClickListener	adapterListener;

		public Builder(Context c) {
			context = c;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setContentView(View v) {
			contentView = v;
			return this;
		}


		public Builder setAdapter(ListAdapter adapter,OnItemClickListener listener)
        {
            this.adapter=adapter;
            this.adapterListener=listener;
            return this;
        }
		public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomAlertDialog create() {

			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CustomAlertDialog dialog = new CustomAlertDialog(context, R.style.dialog);
			View layout = inflater.inflate(R.layout.dialog_custom_layout, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			((TextView) layout.findViewById(R.id.tv_custom_dialog_title)).setText(title);
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.bt_custom_dialog_positive)).setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.bt_custom_dialog_positive))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else
				layout.findViewById(R.id.bt_custom_dialog_positive).setVisibility(View.GONE);
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.bt_custom_dialog_negative)).setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.bt_custom_dialog_negative))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.bt_custom_dialog_negative).setVisibility(View.GONE);
			}
			if (message != null) {
				((TextView) layout.findViewById(R.id.tv_custom_dilaog_message)).setText(message);
			} else if (adapter != null) {
				listView = new ListView(context);
				listView.setAdapter(adapter);
				((LinearLayout) layout.findViewById(R.id.Layout_custom_dialog_content)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.Layout_custom_dialog_content)).addView(listView);
				if (adapterListener != null) {
					listView.setOnItemClickListener(adapterListener);
				}

			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.Layout_custom_dialog_content)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.Layout_custom_dialog_content)).addView(contentView,
						new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			
			Window dialogWindow = dialog.getWindow();
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
			p.height = (int) (wm.getDefaultDisplay().getHeight() * 0.8); // 高度设置为屏幕的0.6
			p.width = (int) (wm.getDefaultDisplay().getWidth() * 0.9); // 宽度设置为屏幕的0.65
			dialogWindow.setAttributes(p);
			return dialog;
		}
	}
}
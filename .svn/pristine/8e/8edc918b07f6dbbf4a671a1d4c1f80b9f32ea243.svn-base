package com.eyunda.third.activities.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eyunda.third.domain.enumeric.ApplyReplyCode;
import com.hy.client.R;

public class AlertWalletHomeDialog {
	private Activity context;
	private View view;
	private Builder ad;
	private RadioGroup mRadioGroup;
	private RadioButton reply;
	private RadioButton noreply;
	protected ApplyReplyCode smpc = ApplyReplyCode.reply;

	public AlertWalletHomeDialog(Activity context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.search_alert_wallethome_item, null);
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		this.context = context;
		this.view = view;
		this.ad = ad;
		
		setview(view);
	}

	private void setview(View view) {
		mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
		reply = (RadioButton) view.findViewById(R.id.reply);
		noreply = (RadioButton) view.findViewById(R.id.noreply);
		mRadioGroup.setOnCheckedChangeListener(mChangeRadio);
	}


	public void showAddDialog(DialogInterface.OnClickListener lis) {

		ad.setTitle("退款处理").setView(view)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setNegativeButton("取消", null).setPositiveButton("确定", lis)
				.show();

	}
	private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == reply.getId()) {
				smpc = ApplyReplyCode.reply;
			} else if (checkedId == noreply.getId()) {
				smpc  = ApplyReplyCode.noreply;
			}
		}
	};

	public ApplyReplyCode getSelect() {
		return smpc;
	}
	
}

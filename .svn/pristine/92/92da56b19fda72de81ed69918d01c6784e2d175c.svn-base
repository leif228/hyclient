package com.eyunda.tools;


import com.hy.client.R;
import com.jungly.gridpasswordview.GridPasswordView;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomPasswordDialog extends Dialog {
	private EditText vCode;
	private GridPasswordView pswView;
	private Button positiveButton, negativeButton, sendCode, clearPwd;
	private TextView title;
	private boolean showVcode;
	LinearLayout vCodeContainer;
	public CustomPasswordDialog(Context c, int theme,boolean showVcode) {
		super(c, theme);
		this.showVcode = showVcode;
		setCustomDialog();
	}

	public CustomPasswordDialog(Context c,boolean showVcode) {
		super(c, R.style.dialog);
		this.showVcode = showVcode;
		setCustomDialog();
	}
	public CustomPasswordDialog(Context c) {
		super(c, R.style.dialog);
		this.showVcode = true;
		setCustomDialog();
		initVcode();
	}
	private void setCustomDialog() {
		View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_passwd, null);
		title = (TextView) mView.findViewById(R.id.title);
		pswView = (GridPasswordView) mView.findViewById(R.id.pswView);
		vCode = (EditText) mView.findViewById(R.id.vCode);
		positiveButton = (Button) mView.findViewById(R.id.positiveButton);
		negativeButton = (Button) mView.findViewById(R.id.negativeButton);
		clearPwd = (Button) mView.findViewById(R.id.clearPwd);
		sendCode = (Button) mView.findViewById(R.id.sendCode);
		vCodeContainer = (LinearLayout)findViewById(R.id.vCodeContainer);
		super.setContentView(mView);
	}
	private void initVcode(){
		if(vCodeContainer != null){
			if(showVcode){
				vCodeContainer.setVisibility(View.VISIBLE);
			}else{
				vCodeContainer.setVisibility(View.GONE);
			}
		}
	}
	public View getPwdView(){
		return pswView;
	}
	public View getValidCodeView(){
		return vCode;
	}
	public View getSendCodeView(){
		return sendCode;
	}
	 @Override
	public void setContentView(int layoutResID) {
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
	}

	@Override
	public void setContentView(View view) {
	}

	/** 
     * 确定键监听器 
     * @param listener 
     */  
    public void setOnPositiveListener(View.OnClickListener listener){  
    	positiveButton.setOnClickListener(listener);  
    }  
    /** 
     * 取消键监听器 
     * @param listener 
     */  
    public void setOnNegativeListener(View.OnClickListener listener){  
    	negativeButton.setOnClickListener(listener);  
    }
    /** 
     * 发送验证码
     * @param listener 
     */  
    public void setOnVCodeListener(View.OnClickListener listener){  
    	sendCode.setOnClickListener(listener);  
    }
    
    public void setOnClearPwdListener(View.OnClickListener listener){
    	clearPwd.setOnClickListener(listener);  
    }
    public void togglePwdText(){
    	pswView.togglePasswordVisibility();
    	if(pswView.getPassWordVisibility()){
    		clearPwd.setText("隐藏密码");
    	}else{
    		clearPwd.setText("显示密码");
    	}
    }
    public String getPwd(){
    	return pswView.getPassWord();
    }
    public String getVCode(){
    	return vCode.getText().toString().trim();
    }
}
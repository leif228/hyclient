package com.eyunda.third.activities.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eyunda.third.common.CommonListActivity;
import com.eyunda.tools.StringUtil;
import com.hy.client.R;

public class EydProtocolActivity extends CommonListActivity implements
		OnClickListener {
	private Button basic_submit;
	private TextView protocol;

	@Override
	public void onStart() {
		super.onStart();
		setTitle("易运达协议");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_register_new_eyd);

		protocol = (TextView) this.findViewById(R.id.protocol);
		basic_submit = (Button) this.findViewById(R.id.basic_submit);
		basic_submit.setOnClickListener(this);
		
		String content = "";
		try {
			InputStream inputStream = getResources().openRawResource(R.raw.eydprotocol);
			content = getString(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} 
//		content = StringUtil.formatHTML(content);
		protocol.setText(content);
	}

    private static String getString(InputStream inputStream) {  
        InputStreamReader inputStreamReader = null;  
        try {  
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        }  
        BufferedReader reader = new BufferedReader(inputStreamReader);  
        StringBuffer sb = new StringBuffer("");  
        String line;  
        try {  
            while ((line = reader.readLine()) != null) {  
                sb.append(line);  
                sb.append("\n");  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return sb.toString();  
    }  

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.basic_submit:
			finish();
			break;
		}
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub

	}

}

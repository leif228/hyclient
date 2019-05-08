package com.eyunda.third.locatedb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hy.client.R;

public class NoNetworkDialog extends Activity {
	  private Button returnButton;  
	    private TextView inputEditor;  
	      
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	      //  requestWindowFeature(Window.FEATURE_NO_TITLE); 
	        setContentView(R.layout.nonetdialog);  
	          
	        returnButton = (Button)findViewById(R.id.returnButton);  
	        inputEditor = (TextView)findViewById(R.id.et);  
	          
	        //和前面一样，只是用到了返回式Activity的基本方法，虽然这里已经是个Dialog了，但却和普通Activity无异   
	        returnButton.setOnClickListener(new OnClickListener() {  
	            public void onClick(View v) {  
	               NoNetworkDialog.this.finish();  
	            }  
	        });  
	    }  
}

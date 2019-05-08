package com.hangyi.zd.activity.gridviewpage;  
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.tools.DateUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.zd.activity.ShowBigImage;
import com.hangyi.zd.activity.gridviewpage.ScrollLayout.OnScreenChangeListenerDataLoad;
import com.hangyi.zd.domain.ShipModelCode;
import com.hangyi.zd.domain.ShipModelData;
import com.hangyi.zd.domain.ShipModelNoData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hangyi.zd.widge.TouchListener;
import com.hy.client.R;
  
/** 
 * GridView分页显示安装的应用程序 
 */  
public class AllAppListActivity extends CommonListActivity {  
    private ScrollLayout mScrollLayout;  
    public static float APP_PAGE_SIZE = 12.0f;  
    public static int APP_PAGE_NumColumns = 3;  
    public static int APP_PAGE_REFRESH = 0;  
    private Context mContext;  
    private PageControlView pageControl;  
    public MyHandler myHandler;  
    public int n=0;  
    private DataLoading dataLoad;
	private ArrayList<ShipModelNoData> list = new ArrayList<ShipModelNoData>();;
	
	private Timer shipImgTimer;
	private ShipImgTimerTask shipImgTask;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
          
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.gridview_main);  
        mContext = this;  
        dataLoad = new DataLoading();  
        mScrollLayout = (ScrollLayout)findViewById(R.id.ScrollLayoutTest);  
        myHandler = new MyHandler(this);  
        
        startShipImgTimer();
    }   
    
    @Override
    protected void onStart() {
    	super.onStart();
    	setTitle("多镜头模式");
    	setRight("设置", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog("图片显视参数设置");
			}
		});
    }
    
    protected void AlertDialog(String title) {
		LayoutInflater  inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_custom_input_layout, null);
		final EditText search_pageno = (EditText) view.findViewById(R.id.search_pageno);
		final EditText search_coluno = (EditText) view.findViewById(R.id.search_coluno);
		search_pageno.setText(APP_PAGE_SIZE+"");
		search_coluno.setText(APP_PAGE_NumColumns+"");
		
		new AlertDialog.Builder(this)
		.setTitle(title)
		.setView(view)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setNegativeButton("取消", null)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String search_pagenostr = search_pageno.getText().toString();
				String search_colunostr = search_coluno.getText().toString();
				 if(TextUtils.isEmpty(search_pagenostr)){
					 Toast.makeText(AllAppListActivity.this, "请输入设置参数", Toast.LENGTH_SHORT).show();
					 search_pageno.requestFocus();
					 return;
				 }
				 if(TextUtils.isEmpty(search_colunostr)){
					 Toast.makeText(AllAppListActivity.this, "请输入设置参数", Toast.LENGTH_SHORT).show();
					 search_coluno.requestFocus();
					 return;
				 }
				 try {
					int search_pagenonum = Integer.valueOf(search_pagenostr);
					int search_colunonum = Integer.valueOf(search_colunostr);
					
					APP_PAGE_SIZE = search_pagenonum;  
				    APP_PAGE_NumColumns = search_colunonum; 
				    
				    showShipImg();
				    
				} catch (NumberFormatException e) {
					Toast.makeText(AllAppListActivity.this, "请输入正确参数值", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}).show();
	
	
}
      
    /** 
     * gridView 的onItemLick响应事件 
     */  
    public OnItemClickListener listener = new OnItemClickListener() {  
  
        public void onItemClick(AdapterView<?> parent, View view, int position,  
                long id) {  
//        	ImageView v = (ImageView)view.findViewById(R.id.imgdetail);  
//        	AlertDialog("查看图片",v);
        	
        	ShipModelNoData smnd = (ShipModelNoData) parent.getItemAtPosition(position);
        	startActivity(new Intent(AllAppListActivity.this,ShowBigImage.class).putExtra("shipId", smnd.getShipID()).putExtra("channel", Integer.valueOf(smnd.getModelNo())).putExtra("shipName", smnd.getShipName()));
        }  
          
    };  
    
    protected void AlertDialog(String title,ImageView img) {
		LayoutInflater  inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_custom_imgbs_layout, null);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
		imageView.setOnTouchListener(new TouchListener(imageView));
		
		imageView.setImageDrawable(img.getDrawable());
		
		new AlertDialog.Builder(mContext)
		.setTitle(title)
		.setView(view)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setNegativeButton("取消", null)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
					
			}
		})
		.show();
	}
      
    @Override  
    protected void onDestroy() {  
        super.onDestroy(); 
        if(shipImgTask!=null){
        	shipImgTask.cancel(); // 将原任务从队列中移除
        	shipImgTask = null;
        }
        if(shipImgTimer!=null){
	        shipImgTimer.cancel();
			shipImgTimer.purge();
			shipImgTimer = null;
        }
    }  
  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        // TODO Auto-generated method stub  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            finish();  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
      
	private void startShipImgTimer() {
		if (shipImgTimer != null) {
			if (shipImgTask != null) {
				shipImgTask.cancel(); // 将原任务从队列中移除
			}
		}else{
			shipImgTimer = new Timer(true);
		}
		shipImgTask = new ShipImgTimerTask();
		shipImgTimer.schedule(shipImgTask,0,1*60*1000); 
	}
	class ShipImgTimerTask extends TimerTask{
		public ShipImgTimerTask() {
		}

		@Override
		public void run() {
			loadShipImg();
		}
	};
	
	private void loadShipImg() {

		if(list.isEmpty()){
			
			SharedPreferences sp = getSharedPreferences("UserPowerData", Context.MODE_PRIVATE);
			String object = sp.getString("UserPower", "");
			
			UserPowerData data = null;
			if(!"".equals(object)){
				Gson gson = new Gson();
				data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
			}else
				data = new UserPowerData();
			
			for(UserPowerShipData upsd:data.getUserPowerShipDatas()){
				for(ShipModelData smd:upsd.getShipModels()){
					if(smd.getModel() == ShipModelCode.five && !smd.getModelNolist().isEmpty()){
						list.addAll(smd.getModelNolist());
						break;
					}
				}
			}
			
		}
		showShipImg();
	}

	private void showShipImg() {
		if(!list.isEmpty()){
			Message msg = new Message();
			msg.what = APP_PAGE_REFRESH;
			AllAppListActivity.this.myHandler.sendMessage(msg);
		}
	}
  
    class MyHandler extends Handler {  
        private AllAppListActivity mContext;  
        public MyHandler(Context conn) {  
            mContext = (AllAppListActivity)conn;  
        }  
  
        public MyHandler(Looper L) {  
            super(L);  
        }  
  
        // 子类必须重写此方法,接受数据  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            super.handleMessage(msg);  
            if (msg.what == APP_PAGE_REFRESH) {  
                      
            	mScrollLayout.removeAllViews();
            	
            	int pageNo = 0;
            	if(APP_PAGE_SIZE == 0)
            		pageNo = 0;
            	else
            		pageNo = (int)Math.ceil(list.size()/APP_PAGE_SIZE);  
                
                for (int i = 0; i < pageNo; i++) {  
                    GridView appPage = new GridView(mContext);  
                    // get the "i" page data  
                    appPage.setAdapter(new AppAdapter(mContext, list, i));  
                    appPage.setNumColumns(APP_PAGE_NumColumns);  
                    appPage.setOnItemClickListener(listener);  
                    mScrollLayout.addView(appPage);  
                }  
                //加载分页  
                pageControl = (PageControlView) findViewById(R.id.pageControl);  
                pageControl.bindScrollViewGroup(mScrollLayout);  
                //加载分页数据  
                dataLoad.bindScrollViewGroup(mScrollLayout);  
                      
                }  
            }  
  
        }  
      
      
    //分页数据  
    class DataLoading {  
        private int count;  
        public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {  
            this.count=scrollViewGroup.getChildCount();  
            scrollViewGroup.setOnScreenChangeListenerDataLoad(new OnScreenChangeListenerDataLoad() {  
                public void onScreenChange(int currentIndex) {  
                    // TODO Auto-generated method stub  
                    //generatePageControl(currentIndex);  
                }  
            });  
        }  
//          
//        private void generatePageControl(int currentIndex){  
//            //如果到最后一页，就加载16条记录  
//            if(count==currentIndex+1){  
//                MyThread m = new MyThread();  
//                new Thread(m).start();  
//            }  
//        }  
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
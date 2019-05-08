package com.hangyi.zd.widge;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.widget.BaseAdapter;

import com.eyunda.main.CommonListActivity;
import com.hy.client.R;

public class MainActivity extends CommonListActivity{

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
	}
//extends AppCompatActivity {

    CommonVideoView videoView;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zd_play_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        videoView = (CommonVideoView) findViewById(R.id.common_videoView);
//        videoView.start("http://192.168.1.132:8080/zdmanage/phone/com.mp4");
    }
    @Override
    protected void onStart() {
    	super.onStart();
    	setTitle("播放器测试");
    }
//
//
//
//    @Override public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            videoView.setFullScreen();
//        }else {
//            videoView.setNormalScreen();
//        }
//    }
}

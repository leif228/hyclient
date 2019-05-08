package com.eyunda.main.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.eyunda.main.data.Image_loader;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.cargo.CargoPreviewActivity;
import com.eyunda.third.activities.home.WebSiteActivity;
import com.eyunda.third.activities.oil.OilListActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.hy.client.R;
import com.ta.TAApplication;


public class SlidAdapter extends PagerAdapter {

    private Context                _context;
    private List<ObjectEntity> _dataSource;
    private List<View>             _views;
    
	private Image_loader imageFetcher;
    
    public SlidAdapter(Context context,List<ObjectEntity> ds,List<View> views,TAApplication application) {

        _context = context;
        _dataSource = ds;
        _views = views;
        imageFetcher=new Image_loader(context, application);
    }

    @Override
    public int getCount() {
        return _views.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        
        container.removeView(_views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container,  int position) {
        
        View itemView = _views.get(position);
        ObjectEntity objectEntity = _dataSource.get(position);
        
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView1);
    	imageFetcher.load_horizontal_Img(objectEntity.imgUrl , imageView);
    	imageView.setTag(position);
    	imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int index=(Integer) v.getTag();
				if(_dataSource.get(index).adtype==null||_dataSource.get(index).name==null){
					return;
				}
				//TODO:首页图片点击事件,根据adtype类型，跳转到不同的显示页面
//				// 1、船舶信息，2、航线信息，3、油品展示页，4、外部链接
				if(_dataSource.get(index).adtype.equals("1")){
					Intent intent = new Intent(_context, CargoPreviewActivity.class);
					intent.putExtra("id", _dataSource.get(index).cid+"");
					intent.putExtra("name",_dataSource.get(index).ename);
					_context.startActivity(intent);
				}
				else if(_dataSource.get(index).adtype.equals("3")){
					//进入油品展示页
					Intent intent = new Intent(_context, OilListActivity.class);
					intent.putExtra("id", _dataSource.get(index).cid+"");
					intent.putExtra("name",_dataSource.get(index).ename);
					_context.startActivity(intent);

//				}else if(_dataSource.get(index).adtype.equals("5")){
//					Intent intent = new Intent(_context,PlugInfo.class);
//					intent.putExtra("id",_dataSource.get(index).name);
//					_context.startActivity(intent);
				}else if(_dataSource.get(index).adtype.equals("4")){
//					Intent intent = new Intent();
//					intent.setAction("android.intent.action.VIEW"); 
//					String url=	_dataSource.get(index).ename;
//					if(!url.startsWith("http://"))url=ApplicationConstants.SERVER_URL+url;
//					try {
//						Uri content_url = Uri.parse(url);  
//						intent.setData(content_url);  
//						_context.startActivity(intent);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					
					Intent intent = new Intent(_context, WebSiteActivity.class);
					intent.putExtra("id", _dataSource.get(index).cid+"");
					String url=	_dataSource.get(index).ename;
					if(!url.startsWith("http://"))url=ApplicationConstants.SERVER_URL+url;
					intent.putExtra("title", _dataSource.get(index).name);
					intent.putExtra("url",url);
					_context.startActivity(intent);
				}else{
					Toast.makeText(_context,_dataSource.get(index).name,Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        container.addView(itemView);
        return itemView;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}
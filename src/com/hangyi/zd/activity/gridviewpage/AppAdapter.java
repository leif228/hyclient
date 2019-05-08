package com.hangyi.zd.activity.gridviewpage;  
  
 
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.lf5.util.Resource;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.tools.CalendarUtil;
import com.eyunda.tools.DateUtils;
import com.hangyi.zd.activity.ContentFragment;
import com.hangyi.zd.activity.newplay.ShipDynamicFragment;
import com.hangyi.zd.domain.ShipModelNoData;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
  
public class AppAdapter extends BaseAdapter {  
    private List<ShipModelNoData> mList;  
    private Context mContext;  
    private PackageManager pm;  
    ImageLoader mImageLoader;
      
    public AppAdapter(Context context, List<ShipModelNoData> list, int page) {  
        mContext = context;  
        pm = context.getPackageManager(); 
        ImageLoader.getInstance().destroy();
        mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance().getImageLoaderConfiguration());
          
        mList = new ArrayList<ShipModelNoData>();  
        int i = page * (int)AllAppListActivity.APP_PAGE_SIZE;  
        int iEnd = i+(int)AllAppListActivity.APP_PAGE_SIZE;  
        while ((i<list.size()) && (i<iEnd)) {  
            mList.add(list.get(i));  
            i++;  
        }  
    }  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return mList.size();  
    }  
  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return mList.get(position);  
    }  
  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  
  
    public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
    	final ShipModelNoData appInfo = mList.get(position);  
        AppItem appItem;  
        if (convertView == null) {  
            View v = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, null);  
              
            appItem = new AppItem();  
            appItem.mAppIcon = (ImageView)v.findViewById(R.id.imgdetail);  
            appItem.mAppName = (TextView)v.findViewById(R.id.tuaninfo);  
              
            v.setTag(appItem);  
            convertView = v;  
        } else {  
            appItem = (AppItem)convertView.getTag();  
        }  
        // set the icon  
//        appItem.mAppIcon.setImageResource(R.drawable.ic_launcher); 
    	mImageLoader.displayImage(ApplicationConstants.ZDPHP_PRE_URL+ "/clientapi/?Function=3&ShipID="+appInfo.getShipID()+"&Channel="+appInfo.getModelNo()+"&PictureTime="+ ContentFragment.getCurrTimeStr(),
    			appItem.mAppIcon,
				NewPageHomeMainActivity.options,new ImageLoadingListener(){

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						((ImageView)view).setImageDrawable(createDrawable(mContext,loadedImage,appInfo.getShipName(),DateUtils.getTime("yyyy-MM-dd HH:mm:ss")));
					}

					@Override
					public void onLoadingFailed(String arg0, View view,
							FailReason arg2) {
						((ImageView)view).setDrawingCacheEnabled(true);
						((ImageView)view).setImageDrawable(createDrawable(mContext,((ImageView)view).getDrawingCache(),appInfo.getShipName(),DateUtils.getTime("yyyy-MM-dd HH:mm:ss")));
						((ImageView)view).setDrawingCacheEnabled(false);
						
					}

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}});
    	
        return convertView;  
    }  
    public static Bitmap createDrawable2(Context mContext,Bitmap imgMarker,String shipName,String time) {  
    	if(imgMarker == null){
//    		Resources res=mContext.getResources();
//    		Drawable drawable=res.getDrawable(R.drawable.home_default);
    		return ShipDynamicFragment.home_defaultBitmap;
    	}
    	int width = imgMarker.getWidth();  
    	int height = imgMarker.getHeight(); 
    	Bitmap imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);  
    	Canvas canvas = new Canvas(imgTemp);  
    	Paint paint = new Paint(); // 建立画笔  
    	paint.setDither(true);  
    	paint.setFilterBitmap(true);  
    	Rect src = new Rect(0, 0, width, height);  
    	Rect dst = new Rect(0, 0, width, height);  
    	canvas.drawBitmap(imgMarker, src, dst, paint);  
    	
    	Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG  
    			| Paint.DEV_KERN_TEXT_FLAG);  
    	textPaint.setTextSize(20.0f);  
    	textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度  
    	textPaint.setColor(Color.RED);  
    	if(shipName != null){
    		canvas.drawText(shipName, 10, 26, textPaint);  
    		canvas.drawText(time, 10, 46, textPaint);  
    	}else
    		canvas.drawText(time, 10, 26, textPaint);  
    	
    	canvas.save(Canvas.ALL_SAVE_FLAG);  
    	canvas.restore();  
    	
    	if(!imgMarker.isRecycled()){
    		imgMarker.recycle();
    		imgMarker=null;
    	}
    	
//        saveMyBitmap(imgTemp,letter);
    	return imgTemp;  
    	
    }  
    public static Drawable createDrawable(Context mContext,Bitmap imgMarker,String shipName,String time) {  
    	if(imgMarker == null){
    		Resources res=mContext.getResources();
    		Drawable drawable=res.getDrawable(R.drawable.home_default);
    		return drawable;
    	}
    	int width = imgMarker.getWidth();  
    	int height = imgMarker.getHeight(); 
         Bitmap imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);  
        Canvas canvas = new Canvas(imgTemp);  
        Paint paint = new Paint(); // 建立画笔  
        paint.setDither(true);  
        paint.setFilterBitmap(true);  
        Rect src = new Rect(0, 0, width, height);  
        Rect dst = new Rect(0, 0, width, height);  
        canvas.drawBitmap(imgMarker, src, dst, paint);  
  
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG  
                | Paint.DEV_KERN_TEXT_FLAG);  
        textPaint.setTextSize(20.0f);  
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度  
        textPaint.setColor(Color.RED);  
        if(shipName != null){
        	canvas.drawText(shipName, 10, 26, textPaint);  
        	canvas.drawText(time, 10, 46, textPaint);  
        }else
        	canvas.drawText(time, 10, 26, textPaint);  
        	
        canvas.save(Canvas.ALL_SAVE_FLAG);  
        canvas.restore(); 
        
//        saveMyBitmap(imgTemp,letter);
        return (Drawable) new BitmapDrawable(mContext.getResources(),imgTemp);  
  
    }  
    public static void saveMyBitmap(Bitmap bitmap, String bitName) { 
    	String path = "/mnt/sdcard/zd/log2/";  
        File dir = new File(path);  
        if (!dir.exists()) {  
            dir.mkdirs();  
        }  
        File f = new File(path + bitName + ".png");  
        FileOutputStream fOut = null;  

        try {
        	f.createNewFile(); 
			fOut = new FileOutputStream(f);  

			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);  

			fOut.flush();  

			fOut.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }
  
    /** 
     * 每个应用显示的内容，包括图标和名称 
     * @author Yao.GUET 
     * 
     */  
    class AppItem {  
        ImageView mAppIcon;  
        TextView mAppName;  
    }  
   
}  


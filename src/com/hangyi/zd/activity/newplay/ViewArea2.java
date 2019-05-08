package com.hangyi.zd.activity.newplay;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.location.ShipCooordData;
import com.hangyi.zd.activity.dialog.CustomDialog;
import com.hangyi.zd.activity.dialog.TouchView;
import com.hangyi.zd.activity.gridviewpage.AppAdapter;
import com.hangyi.zd.domain.ShipGpsData;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 前面说了ViewArea是一个布局， 所以这里当然要继承一个布局了,LinearLayout也可以
 * @author kun.zhang
 * @since DSM Appstore
 */
public class ViewArea2 extends FrameLayout
{
    private int imgDisplayW;
    private int imgDisplayH;
    private int imgW;
    private int imgH;
    private TouchView touchView;
    ImageLoader mImageLoader;
    ShipCooordData curShip;
    Context context;
    int i;


    // private DisplayMetrics dm;

    /**
     * 构造函数
     * @param b 
     * @param context
     * @param view 
     * @param resId 图片资源id,当然也可以用别的方式获取图片
     */
    public ViewArea2(final CustomDialog.Builder b, Context context,ShipGpsData curShip,int i, ImageView view)
    {
        super(context);
        this.context=context;
        this.i =i;
        ImageLoader.getInstance().destroy();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance().getImageLoaderConfiguration());

        // 这里的宽高要和xml中的LinearLayout大小一致，如果要指定大小。
        // xml中LinearLayout的宽高一定要用px像素单位，因为这里的宽高是像素，用dp会有误差！
        imgDisplayW = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        imgDisplayH = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();

        // 这句就是我们的自定义ImageView
        touchView = new TouchView(context, imgDisplayW, imgDisplayH,view,b);
        touchView.setAdjustViewBounds(true);
        // 给我们的自定义imageView设置要显示的图片
        // touchView.setImageResource(resId);
//        touchView.setImageBitmap(resId);
//        touchView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				b.dismiss();
//			}
//		});
//        NewContentFragment.displayImage(mImageLoader,context,touchView,curShip,i);
        
        String imgStr31 = ShipDynamicFragment.getUrl(curShip,String.valueOf(i));
		if(imgStr31 !=null )
			touchView.setImageDrawable(AppAdapter.createDrawable(context, BitmapFactory.decodeFile(imgStr31), null, curShip.getGpsTime()));
        

        // Bitmap img =
        // BitmapFactory.decodeResource(context.getResources(),
        // resId);
//        imgW = resId.getWidth();
//        imgH = resId.getHeight();

        // 图片第一次加载进来，判断图片大小从而确定第一次图片的显示方式。
//        int layout_w = imgW > imgDisplayW ? imgDisplayW : imgDisplayW;
//        int layout_h = imgH > imgDisplayH ? imgDisplayH : imgDisplayH;

        // 下面的代码是判断图片初始显示样式的，当然可以根据你的想法随意显示，
        // 我这里是将宽大于高的图片按照宽缩小的比例把高压缩，
        // 前提必须是宽度超出了屏幕大小，相反，如果高大于宽，我将图片按照高缩小的比例把宽压缩，
        // 前提必须是高度超出了屏幕大小
//        if (imgW >= imgH)
//        {
//            if (layout_w == imgDisplayW)
//            {
//                layout_h = (int) (imgH * ((float) imgDisplayW / imgW));
//            }
//        }
//        else
//        {
//            if (layout_h == imgDisplayH)
//            {
//                layout_w = (int) (imgW * ((float) imgDisplayH / imgH));
//            }
//        }

        // 这里需要注意的是，采用FreamLayout或者LinearLayout的好处是：
        // 如果压缩后的图片仍有一个边大于屏幕， 那么只显示在屏幕内的部分，可以通过移动后看见外部（不会裁剪掉图片），
        // 如果采用RelativeLayout布局，图片会始终完整显示在屏幕内部，不会有超出屏幕的现象；
        // 如果图片不是完全占满屏幕，那么在屏幕上没有图片的地方拖动，图片也会移动，这样的体验不太好，
        // 建议用FreamLayout或者LinearLayout。

        // 这是自定义imageView的大小，也就是触摸范围
        FrameLayout.LayoutParams paramst = new FrameLayout.LayoutParams(
        		imgDisplayW-50, imgDisplayH*3/5);
            paramst.gravity = Gravity.CENTER_HORIZONTAL;
            touchView.setLayoutParams(paramst);
            
        
        //图片放大50
//        int w = view.getWidth()+50;
//        int h = view.getHeight()+50;
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( w, h);
//        
//        int[] location = new int[2];  
//        view.getLocationOnScreen(location);  
//        //弹出框父控件不包括标题栏x轴不变，y轴加50
//        int screenx = location[0];  
//        int screeny = location[1]-50;  
//        layoutParams.topMargin=screeny;
//        layoutParams.leftMargin=screenx;
//        layoutParams.rightMargin=screenx+w;
//        layoutParams.bottomMargin=screeny+h;
//        touchView.setLayoutParams(layoutParams);
        
        this.addView(touchView);
    }
//    private void displayImage(ImageView view,final ShipCooordData curShip,int i ){
//		mImageLoader.displayImage(ApplicationConstants.ZDPHP_PRE_URL+ "/clientapi/?Function=3&ShipID="+curShip.getShipID()+"&Channel="+i+"&PictureTime="+ NewContentFragment.getUrlTimeStr(curShip.getGpsTime()),
//				view,
//				NewPageHomeMainActivity.options,new ImageLoadingListener(){
//
//			@Override
//			public void onLoadingCancelled(String arg0, View arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(getActivity(),loadedImage,null,DateUtils.getTime("yyyy-MM-dd HH:mm:ss")));
//				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(context,loadedImage,null,curShip.getGpsTime()));
//			}
//
//			@Override
//			public void onLoadingFailed(String arg0, View view,
//					FailReason arg2) {
//				((ImageView)view).setDrawingCacheEnabled(true);
//				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(context,((ImageView)view).getDrawingCache(),null,curShip.getGpsTime()));
//				((ImageView)view).setDrawingCacheEnabled(false);
//				
//			}
//
//			@Override
//			public void onLoadingStarted(String arg0, View arg1) {
//				// TODO Auto-generated method stub
//				
//			}});
//	}
    
//    public void changImg(ShipCooordData curShip){
//    	NewContentFragment.displayImage(mImageLoader,context,touchView,curShip,i);
//    }
    public void playImg(ShipGpsData curShip){
    	String imgStr31 = ShipDynamicFragment.getUrl(curShip,String.valueOf(i));
		if(imgStr31 !=null )
			touchView.setImageDrawable(AppAdapter.createDrawable(context, BitmapFactory.decodeFile(imgStr31), null, curShip.getGpsTime()));
		else
			touchView.setImageDrawable(AppAdapter.createDrawable(context, ShipDynamicFragment.lose_imgBitmap, null, curShip.getGpsTime()));
    }

	public void playLoseImg(String time) {
    	touchView.setImageDrawable(AppAdapter.createDrawable(context, ShipDynamicFragment.lose_gpsBitmap, null, time));
    }
    

}

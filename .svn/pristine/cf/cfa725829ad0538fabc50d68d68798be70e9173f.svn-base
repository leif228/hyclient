package com.hangyi.zd.widge;


import java.io.FileInputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 图片浏览器
 * @author Keynes Cao
 * @create 2011-12-5 上午9:24:23
 * @package com.farben.ams.widget
 */
public class ImageSurfaceView  extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    //图片集
    private List<String>         mList            =    null;
    //运行状态
    public     boolean             mLoop             =     false;
    //获取画布
    private SurfaceHolder         mSurfaceHolder     =     null;
    //图片索引
    private int                 mCount             =     0; 
    //时间间隔
    private long                 speed             =     1000;
    
    private static Matrix         matrix             =     new Matrix();
    /**
     * @param context
     * <see>容器</see>
     * @param list
     * <see>图片地址列表 </see>
     * @param rate
     * <see>图片切换时间　单位:毫秒</see>
     * 
     */
    public ImageSurfaceView(Context context,List<String>list,long speed) {
        super(context);
        if(list!=null && list.size()>0){
            this.mList = list;
            this.speed = speed;
        }
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mLoop = true;//开始画图
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,    int height) {}
    //图像创建时
    public void surfaceCreated(SurfaceHolder holder) {
        if(mList!=null && mList.size()>0){
            if(mList.size() == 1){
                Log.d("ImageSurfaceView"," Only one picture");
                drawImg();
            }else{
                Log.d("ImageSurfaceView"," run Thread.");
                new Thread(this).start();
            }            
        }
        
    }
    //视图销毁时
    public void surfaceDestroyed(SurfaceHolder holder) {
        mLoop = false;
    }
    //画图方法
    private void drawImg(){
        Canvas canvas = mSurfaceHolder.lockCanvas();
        if(canvas == null || mSurfaceHolder == null){
            return;
        }
        if(mCount >= mList.size()){
            mCount = 0;
        }
        Bitmap bitmap  = null;
        try{
            String path = mList.get(mCount++);
            bitmap  = BitmapFactory.decodeStream(new FileInputStream(path));
            if(bitmap!=null){            
                //画布宽和高
                int height = getHeight();
                int width  = getWidth();
                //生成合适的图像
                bitmap = getReduceBitmap(bitmap,width,height);
                
                Paint paint = new Paint();            
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL);
                //清屏
                paint.setColor(Color.BLACK);
                canvas.drawRect(new Rect(0, 0, getWidth(),getHeight()), paint);        
                //Log.d("ImageSurfaceView_IMG",path);
                //画图
                canvas.drawBitmap(bitmap, matrix, paint);    
            }
            //解锁显示
            mSurfaceHolder.unlockCanvasAndPost(canvas);                
        }catch(Exception ex){
            Log.e("ImageSurfaceView",ex.getMessage());
            return;
        }finally{
            //资源回收
            if(bitmap!=null){
                bitmap.recycle();
            }
        }        
    }    
    //刷新图片
    public void run() {
        while(mLoop){            
            synchronized (mSurfaceHolder) {
                drawImg();
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                Log.e("ImageSurfaceView_Thread",e.getMessage());
            }
        }
        mList = null;//消毁
    }
    //缩放图片
    private Bitmap getReduceBitmap(Bitmap bitmap ,int w,int h){
        int     width     =     bitmap.getWidth();
        int     hight     =     bitmap.getHeight();
        Matrix     matrix     =     new Matrix();
        float     wScake     =     ((float)w/width); 
        float     hScake     =     ((float)h/hight);        
        matrix.postScale(wScake, hScake);        
        return Bitmap.createBitmap(bitmap, 0,0,width,hight,matrix,true);
    }

}
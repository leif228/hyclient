package com.hangyi.zd.activity.dialog;

import com.baidu.android.bbalbs.common.a.d;

import android.content.Context;
import android.graphics.Canvas;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class TouchView extends ImageView
{
    /**
     * 表示当前没有状态
     */
    public static final int NONE = 0;

    /**
     * 表示当前处于移动状态
     */
    public static final int DRAG = 1;

    /**
     * 表示当前处于缩放状态
     */
    public static final int ZOOM = 2;

    /**
     * 表示放大图片
     */
    public static final int BIGGER = 3;

    /**
     * 表示缩小图片
     */
    public static final int SMALLER = 4;

    /**
     * mode用于标示当前处于什么状态
     */
    private int mode = NONE;

    /**
     * 第一次触摸两点的距离
     */
    private float beforeLenght;

    /**
     * 移动后两点的距离
     */
    private float afterLenght;

    /**
     * 缩放因子 缩放的比例 X Y方向都是这个值 越大缩放的越快  
     */
    private float scale = 0.02f;//

    /**
     * 下面两句图片的移动范围，及ViewArea的范围，
     * 也就是linearLayout的范围，也就是屏幕方位（都是填满父控件属性）
     */
    private int screenW;

    /**
     * 屏幕高度
     */
    private int screenH;

    /**
     * 开始触摸点
     */
    private int start_x;

    /**
     * 开始触摸点
     */
    private int start_y;

    /**
     * 结束触摸点
     */
    private int stop_x;

    /**
     * 结束触摸点
     */
    private int stop_y;

    /**
     * 回弹动画
     */
    private TranslateAnimation trans;
    
    int layoutleft = 0, layouttop = 0, layoutright = 500, layoutbottom = 500;
    int frameleft = 0, frametop = 0, frameright = 500 , framebottom = 500;
    boolean scaleFlag = false;
    ImageView view;
    CustomDialog.Builder b;
	int count = 0;   
	boolean firClickFlag = false;   
	int firClick = 0;   
	int secClick = 0;   


    /**
     * 构造函数 这里传进来的w，h就是图片的移动范围
     * @param context
     * @param w
     * @param h
     * @param view 
     */
    public TouchView(Context context, int w, int h, ImageView view,CustomDialog.Builder b)
    {
        super(context);
        this.view = view;
        this.b = b;
        this.setPadding(0, 0, 0, 0);
        screenW = w;
        screenH = h;
        
        this.layoutleft = view.getLeft();
        this.layouttop = view.getTop();
        this.layoutright = view.getRight();
        this.layoutbottom = view.getBottom();
        
        
    }


    /**
     * 用来计算2个触摸点的距离
     * @param event
     * @return
     */
    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
        // MotionEvent.ACTION_MASK表示多点触控事件
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;

                // 表示相对于屏幕左上角为原点的坐标
                stop_x = (int) event.getRawX();

                // 同上
                stop_y = (int) event.getRawY();

                start_x = stop_x - this.getLeft();

                start_y = stop_y - this.getTop();

                if (event.getPointerCount() == 2)
                    beforeLenght = spacing(event);
                
                //处理单击、双击
				count++;
				if (count == 1) {
					firClick = (int) System.currentTimeMillis();
					firClickFlag = true;
				} else if (count == 2) {
					secClick = (int) System.currentTimeMillis();
					// 双击事件
					if (secClick - firClick < 1000)
						setScale(0.1f, BIGGER);
					count = 0;
					firClick = 0;
					secClick = 0;
				}
                
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            	firClickFlag = false;
            	count = 0;
                if (spacing(event) > 10f)
                {
                    mode = ZOOM;
                    beforeLenght = spacing(event);
                }
                break;
            case MotionEvent.ACTION_UP:
            	if(firClickFlag)
            		b.dismiss();

                int disX = 0;
                int disY = 0;
                if (getHeight() <= screenH)
                {
                    if (this.getTop() < 0)
                    {
                        disY = getTop();
                        this.layout(this.getLeft(), 0, this.getRight(),
                            0 + this.getHeight());

                    }
                    else if (this.getBottom() >= screenH)
                    {
                        disY = getHeight() - screenH + getTop();
                        this.layout(this.getLeft(), screenH - getHeight(),
                            this.getRight(), screenH);
                    }
                }
                else
                {
                    int Y1 = getTop();
                    int Y2 = getHeight() - screenH + getTop();
                    if (Y1 > 0)
                    {
                        disY = Y1;
                        this.layout(this.getLeft(), 0, this.getRight(),
                            0 + this.getHeight());
                    }
                    else if (Y2 < 0)
                    {
                        disY = Y2;
                        this.layout(this.getLeft(), screenH - getHeight(),
                            this.getRight(), screenH);
                    }
                }
                if (getWidth() <= screenW)
                {
                    if (this.getLeft() < 0)
                    {
                        disX = getLeft();
                        this.layout(0, this.getTop(), 0 + getWidth(),
                            this.getBottom());
                    }
                    else if (this.getRight() > screenW)
                    {
                        disX = getWidth() - screenW + getLeft();
                        this.layout(screenW - getWidth(), this.getTop(),
                            screenW, this.getBottom());
                    }
                }
                else
                {
                    int X1 = getLeft();
                    int X2 = getWidth() - screenW + getLeft();
                    if (X1 > 0)
                    {
                        disX = X1;
                        this.layout(0, this.getTop(), 0 + getWidth(),
                            this.getBottom());
                    }
                    else if (X2 < 0)
                    {
                        disX = X2;
                        this.layout(screenW - getWidth(), this.getTop(),
                            screenW, this.getBottom());
                    }

                }
                // 如果图片缩放到宽高任意一个小于100，那么自动放大，直到大于100.
                while (getHeight() < 100 || getWidth() < 100)
                {

                    setScale(scale, BIGGER);
                }
                // 根据disX和disY的偏移量采用移动动画回弹归位，动画时间为500毫秒。
                if (disX != 0 || disY != 0)
                {
                    trans = new TranslateAnimation(disX, 0, disY, 0);
                    trans.setDuration(500);
                    this.startAnimation(trans);
                }
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
            	if((event.getRawX()>stop_x&&(event.getRawX()-stop_x>2))
            			||(stop_x>event.getRawX()&&(stop_x-event.getRawX()>2))){
            		firClickFlag = false;
            		count = 0;
            	}
            	
                if (mode == DRAG)
                {
                    // 执行拖动事件的时，不断变换自定义imageView的位置从而达到拖动效果
                    this.setPosition(stop_x - start_x, stop_y - start_y,
                        stop_x + this.getWidth() - start_x, stop_y - start_y
                                                            + this.getHeight());
                    stop_x = (int) event.getRawX();
                    stop_y = (int) event.getRawY();

                }
                else if (mode == ZOOM)
                {
                    if (spacing(event) > 10f)
                    {
                        afterLenght = spacing(event);
                        float gapLenght = afterLenght - beforeLenght;
                        if (gapLenght == 0)
                        {
                            break;
                        }
                        // 图片宽度（也就是自定义imageView）必须大于70才可以缩放
                        else if (Math.abs(gapLenght) > 5f && getWidth() > 70)
                        {
                            if (gapLenght > 0)
                            {
                                this.setScale(scale, BIGGER);
                            }
                            else
                            {
                                this.setScale(scale, SMALLER);
                            }
                            beforeLenght = afterLenght; // 这句不能少。
                        }
                    }
                }
                break;
        }
        return true;
    }

    /**   
     * 实现处理缩放   
     */   
    private void setScale(float temp, int flag)
    {

        if (flag == BIGGER)
        {
            // setFrame(left , top,
            // right,bottom)函数表示改变当前view的框架，也就是大小。
        	
            this.setFrame(this.getLeft() - (int) (temp * this.getWidth()),
                this.getTop() - (int) (temp * this.getHeight()),
                this.getRight() + (int) (temp * this.getWidth()),
                this.getBottom() + (int) (temp * this.getHeight()));
            
            this.frameleft = this.getLeft() - (int) (temp * this.getWidth());
            this.frametop = this.getTop() - (int) (temp * this.getHeight());
            this.frameright = this.getRight() + (int) (temp * this.getWidth());
            this.framebottom = this.getBottom() + (int) (temp * this.getHeight());
            
            scaleFlag = true;
            
        }
        else if (flag == SMALLER)
        {
        	
            this.setFrame(this.getLeft() + (int) (temp * this.getWidth()),
                this.getTop() + (int) (temp * this.getHeight()),
                this.getRight() - (int) (temp * this.getWidth()),
                this.getBottom() - (int) (temp * this.getHeight()));
            
            this.frameleft = this.getLeft() + (int) (temp * this.getWidth());
            this.frametop = this.getTop() + (int) (temp * this.getHeight());
            this.frameright = this.getRight() - (int) (temp * this.getWidth());
            this.framebottom = this.getBottom() - (int) (temp * this.getHeight());
            
            scaleFlag = true;
            
        }
    }

    /**   
     * 实现处理拖动   
     */    
    private void setPosition(int left, int top, int right, int bottom)
    {
    	
        this.layout(left, top, right, bottom);
        
        this.layoutleft = left;
        this.layouttop = top;
        this.layoutright = right;
        this.layoutbottom = bottom;
        
    }
    
    @Override  
    protected void onDraw(Canvas canvas) {   //这里就是重写的方法了，想画什么形状自己动手  
        super.onDraw(canvas);  
        
        if(mode != DRAG && mode != ZOOM && !scaleFlag){
//        	this.setFrame(frameleft, frametop, frameright, framebottom);
//        	this.layout(layoutleft, layouttop, layoutright, layoutbottom);
        }
    }

}

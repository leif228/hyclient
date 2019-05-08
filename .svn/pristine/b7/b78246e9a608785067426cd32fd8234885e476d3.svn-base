package com.eyunda.main.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class MyViewPager extends ViewPager{
	private GestureDetector mGestureDetector; 
	private View.OnTouchListener mGestureListener; 

	private static final String TAG = "CustomHScrollView"; 


	        /** 
	         * @function CustomHScrollView constructor 
	         * @param context  Interface to global information about an application environment. 
	         * 
	         */ 
	        public MyViewPager(Context context) { 
	                super(context); 
	                // TODO Auto-generated constructor stub 
	            mGestureDetector = new GestureDetector(new HScrollDetector()); 
	            setFadingEdgeLength(0); 
	        } 
	        
	        
	        /** 
	         * @function CustomHScrollView constructor  
	         * @param context Interface to global information about an application environment. 
	         * @param attrs A collection of attributes, as found associated with a tag in an XML document. 
	         */ 
	        public MyViewPager(Context context, AttributeSet attrs) { 
	            super(context, attrs); 
	        mGestureDetector = new GestureDetector(new HScrollDetector()); 
	        setFadingEdgeLength(0); 
	        } 

	    
	        
	        @Override 
	   public boolean onInterceptTouchEvent(MotionEvent ev) { 
	        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev); 
	   } 
	     
	       // Return false if we're scrolling in the y direction   
	   class HScrollDetector extends SimpleOnGestureListener { 
	           @Override 
	       public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {        
	            if(Math.abs(distanceX) > Math.abs(distanceY)) { 
	                   return true; 
	            } 
	           
	            return false; 
	       } 
	   } 
}

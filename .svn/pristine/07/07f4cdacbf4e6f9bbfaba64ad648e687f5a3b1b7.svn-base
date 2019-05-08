package com.eyunda.main.reg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PhotoUtil {
	public Bitmap getBitmapFormPath(String filePath){
		File file = new File(filePath);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
		 
		}
		 int wh[]=  new int[]{720,1280};
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 为true里只读图片的信息，如果长宽，返回的bitmap为null
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inDither = false;
		/**
		 * 计算图片尺寸 //TODO 按比例缩放尺寸
		 */
		BitmapFactory.decodeFile(filePath, options);

		int bmpheight = options.outHeight;
		int bmpWidth = options.outWidth;
		int inSampleSize = bmpheight / wh[1] > bmpWidth / wh[0] ? bmpheight / wh[1] : bmpWidth / wh[0];
		// if(bmpheight / wh[1] < bmpWidth / wh[0]) inSampleSize = inSampleSize * 2 / 3;//TODO 如果图片太宽而高度太小，则压缩比例太大。所以乘以2/3
		if (inSampleSize > 1)
			options.inSampleSize = inSampleSize;// 设置缩放比例
		options.inJustDecodeBounds = false;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is, null, options);
		} catch (OutOfMemoryError e) {
			
			System.gc();
			bitmap = null;
		}
		return bitmap;
		
	}
	public void save(Bitmap bitmap,String filePath){
		int offset = 100;
		File file = new File(filePath);
		long fileSize = file.length();
		if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)
			offset = 90;
		else if (1024 * 1024 < fileSize)
			offset = 85;
		else if (1024 * 1024*2 < fileSize)
			offset = 65;
		
		FileOutputStream fOut = null;  
		try {  
			          fOut = new FileOutputStream(file);  
		     } catch (FileNotFoundException e) {  
		             e.printStackTrace();  
			    }  

	
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, offset, fOut);
		
		
		  try {  
	             fOut.flush();  
	     } catch (IOException e) {  
	              e.printStackTrace();  
	   }  
	     try {  
	            fOut.close();  
	    } catch (IOException e) {  
	            e.printStackTrace();  
	    }
		
		
		
	}
	public   void createNewBitmapAndCompressByFile(String filePath) {
			int offset = 100;
			 int wh[]=  new int[]{720,1280};
			File file = new File(filePath);
			long fileSize = file.length();
			if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)
				offset = 90;
			else if (1024 * 1024 < fileSize)
				offset = 85;
			else if (1024 * 1024*2 < fileSize)
				offset = 65;
//			if (offset == 100)
//				return  ;// 缩小质量
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true; // 为true里只读图片的信息，如果长宽，返回的bitmap为null
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inDither = false;
			/**
			 * 计算图片尺寸 //TODO 按比例缩放尺寸
			 */
			BitmapFactory.decodeFile(filePath, options);
	
			int bmpheight = options.outHeight;
			int bmpWidth = options.outWidth;
			int inSampleSize = bmpheight / wh[1] > bmpWidth / wh[0] ? bmpheight / wh[1] : bmpWidth / wh[0];
			// if(bmpheight / wh[1] < bmpWidth / wh[0]) inSampleSize = inSampleSize * 2 / 3;//TODO 如果图片太宽而高度太小，则压缩比例太大。所以乘以2/3
			if (inSampleSize > 1)
				options.inSampleSize = inSampleSize;// 设置缩放比例
			options.inJustDecodeBounds = false;
	
			InputStream is = null;
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
			 
			}
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(is, null, options);
			} catch (OutOfMemoryError e) {
				
				System.gc();
				bitmap = null;
			}
			FileOutputStream fOut = null;  
			try {  
				          fOut = new FileOutputStream(file);  
			     } catch (FileNotFoundException e) {  
			             e.printStackTrace();  
				    }  

		
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, offset, fOut);
//			byte[] buffer = baos.toByteArray();
//			options = null;
//			if (buffer.length >= fileSize)
//				return bitmap;
//			return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
			  try {  
				             fOut.flush();  
				     } catch (IOException e) {  
				              e.printStackTrace();  
				   }  
				     try {  
				            fOut.close();  
				    } catch (IOException e) {  
				            e.printStackTrace();  
				    }  

		}
}


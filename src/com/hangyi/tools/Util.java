package com.hangyi.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;

import com.eyunda.third.GlobalApplication;

public class Util {
	public static String getFlightTimeByMins(String mins) {
		if (!TextUtils.isEmpty(mins) ) {
			try {
				Double minsd = Double.valueOf(mins);
				long between = Double.valueOf(minsd * 60).longValue();// 转换成秒
				long day = between / (24 * 3600);
				long hour = between % (24 * 3600) / 3600;
				long min = between % (24 * 3600) % 3600 / 60;
				StringBuilder sb = new StringBuilder();
				if (0l != day)
					sb.append(day + "天");
				if (0l != hour)
					sb.append(hour + "小时");
				if (0l != min)
					sb.append(min + "分");
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	public static String getFlightTimeOnLine(String depTime, String arrTime) {
		if (!TextUtils.isEmpty(depTime) && !TextUtils.isEmpty(arrTime)) {
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date begin = null;
			java.util.Date end = null;
			try {
				begin = dfs.parse(depTime);
				end = dfs.parse(arrTime);
				long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
				long day = between / (24 * 3600);
				long hour = between % (24 * 3600) / 3600;
				long min = between % (24 * 3600) % 3600 / 60;
				StringBuilder sb = new StringBuilder();
				if (0l != day)
					sb.append(day + "天");
				if (0l != hour)
					sb.append(hour + "小时");
				if (0l != min)
					sb.append(min + "分");
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	} 

	 public static Bitmap getLoacalBitmap(String url) {
	     try {
	          FileInputStream fis = new FileInputStream(url);
	          return BitmapFactory.decodeStream(fis);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	
	public static String secToTime(int time) {  
        String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
  
    public static String unitFormat(int i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    }
	
	public static String IntToHex2(int n){  
        char[] ch = new char[20];  
        int nIndex = 0;  
        while ( true ){  
            int m = n/16;  
            int k = n%16;  
            if ( k == 15 )  
                ch[nIndex] = 'F';  
            else if ( k == 14 )  
                ch[nIndex] = 'E';  
            else if ( k == 13 )  
                ch[nIndex] = 'D';  
            else if ( k == 12 )  
                ch[nIndex] = 'C';  
            else if ( k == 11 )  
                ch[nIndex] = 'B';  
            else if ( k == 10 )  
                ch[nIndex] = 'A';  
            else   
                ch[nIndex] = (char)('0' + k);  
            nIndex++;  
            if ( m == 0 )  
                break;  
            n = m;  
        }  
        StringBuffer sb = new StringBuffer();  
        sb.append(ch, 0, nIndex);  
        sb.reverse();  
        String strHex = new String("0x");  
        strHex += sb.toString();  
        return strHex;  
    }  


	public static Bitmap createDrawable(Context mContext, Bitmap imgMarker,
			String shipName) {
		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		textPaint.setTextSize(24.0f);
		textPaint.setTypeface(Typeface.DEFAULT); // 采用默认的宽度
		textPaint.setColor(0xFFFF00FF); // .setColor(0xAACC0000);.bgColor(0xAAFFFF00).fontSize(28)
										// .fontColor(0xFFFF00FF)
		// draw text to the Canvas center
		Rect bounds = new Rect();
		textPaint.getTextBounds(shipName, 0, shipName.length(), bounds);

		int width = imgMarker.getWidth();
		int height = imgMarker.getHeight();

		Paint paint = new Paint(); // 建立画笔
		paint.setDither(true);
		paint.setFilterBitmap(true);

		Bitmap imgTemp;
		Canvas canvas;
		if (bounds.width() > width) {
			imgTemp = Bitmap.createBitmap(bounds.width(),
					height + bounds.height(), Bitmap.Config.ARGB_8888);
		} else {
			imgTemp = Bitmap.createBitmap(width, height + bounds.height(),
					Bitmap.Config.ARGB_8888);
		}
		canvas = new Canvas(imgTemp);

		Rect src = new Rect(0, 0, width, height);
		Rect dst = new Rect((imgTemp.getWidth() - width) / 2, 0,
				(imgTemp.getWidth() - width) / 2 + width, height);
		canvas.drawBitmap(imgMarker, src, dst, paint);
		
		// 画矩形背景
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);// 去掉边缘锯齿
		mPaint.setColor(0xAAFFFF00);
		canvas.drawRect(0, height, imgTemp.getWidth(), imgTemp.getHeight(),
				mPaint);
		
//		x默认是‘www.jcodecraeer.com’这个字符串的左边在屏幕的位置，
//		如果设置了paint.setTextAlign(Paint.Align.CENTER);那就是字符的中心，
//		y是指定这个字符baseline在屏幕上的位置。
		canvas.drawText(shipName, 0, imgTemp.getHeight()-2, textPaint);

		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();

		// saveMyBitmap(imgTemp,letter);
		return new BitmapDrawable(mContext.getResources(), imgTemp).getBitmap();
	}
	
	public static Bitmap first(Resources res, Bitmap roteBitmapOrg,
			float degrees, Bitmap baseBitmapOrg) {
		// 防止出现Immutable bitmap passed to Canvas constructor错误

		Bitmap rotedBitmap = roteImge(roteBitmapOrg, degrees);

		Bitmap newBitmap = null;
		newBitmap = Bitmap.createBitmap(baseBitmapOrg);
		Canvas canvas = new Canvas(newBitmap);
		Paint paint = new Paint();
		int w = baseBitmapOrg.getWidth();
		int h = baseBitmapOrg.getHeight();
		int w_2 = rotedBitmap.getWidth();
		int h_2 = rotedBitmap.getHeight();
		paint.setColor(Color.GRAY);
		paint.setAlpha(0);
		canvas.drawRect(0, 0, baseBitmapOrg.getWidth(),
				baseBitmapOrg.getHeight(), paint);
		paint = new Paint();
		canvas.drawBitmap(rotedBitmap, 0, 0, paint);
		// canvas.drawBitmap(rotedBitmap, Math.abs(w - w_2) / 2,
		// Math.abs(h - h_2) / 2, paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		// 存储新合成的图片
		canvas.restore();
		return newBitmap;
	}

	/*
	 * Android实现图片旋转 后合成新图
	 */
	public static Bitmap roteComposeImge(Resources res, Bitmap roteBitmapOrg,
			float degrees, Bitmap baseBitmapOrg) {
		Bitmap rotedBitmap = roteImge(roteBitmapOrg, degrees);

		Drawable[] array = new Drawable[2];
		array[0] = new BitmapDrawable(res, baseBitmapOrg);
		array[1] = new BitmapDrawable(res, rotedBitmap);
		LayerDrawable la = new LayerDrawable(array);
		// 其中第一个参数为层的索引号，后面的四个参数分别为left、top、right和bottom
		la.setLayerInset(0, 0, 0, 0, 0);
		la.setLayerInset(1, 0, 0, 0, 0);

		return drawableToBitmap(la);
	}

	/*
	 * Drawable → Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap.createBitmap(

		drawable.getIntrinsicWidth(),

		drawable.getIntrinsicHeight(),

		drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		// canvas.setBitmap(bitmap);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());

		drawable.draw(canvas);

		return bitmap;

	}

	/*
	 * Android实现图片缩放与旋转
	 */
	private static Bitmap roteImge(Bitmap bitmapOrg, float degrees) {

		// // 获取这个图片的宽和高
		// int width = bitmapOrg.getWidth();
		// int height = bitmapOrg.getHeight();

		// 定义预转换成的图片的宽度和高度
		// int newWidth = bitmapOrg.getWidth();
		// int newHeight = bitmapOrg.getHeight();
		//
		// // 计算缩放率，新尺寸除原始尺寸
		// float scaleWidth = ((float) newWidth) / width;
		// float scaleHeight = ((float) newHeight) / height;
		//
		// // 创建操作图片用的matrix对象
		// Matrix matrix = new Matrix();
		//
		// // 缩放图片动作
		// matrix.postScale(scaleWidth, scaleHeight);
		//
		// // 旋转图片 动作
		// matrix.setRotate(degrees, width/2, height/2);
		// // .postRotate(degrees);
		//
		// // 创建新的图片
		// Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
		// height, matrix, true);

		// 创建一个和原图一样大小的图片
		
//		因为图像的变换是针对每一个像素点的，所以有些变换可能发生像素点的丢失，
//		这里需要使用Paint.setAnitiAlias(boolean)设置来消除锯齿，这样图片变换后的效果会好很多。
		Paint paint = new Paint();
		 paint.setAntiAlias(true);
		// paint.setColor(Color.GRAY);
		// paint.setAlpha(0);
		Bitmap afterBitmap = Bitmap.createBitmap(bitmapOrg.getWidth(),
				bitmapOrg.getHeight(), bitmapOrg.getConfig());
		Canvas canvas = new Canvas(afterBitmap);
		Matrix matrix = new Matrix();
		// 根据原图的中心位置旋转
		matrix.setRotate(degrees, bitmapOrg.getWidth() / 2,
				bitmapOrg.getHeight() / 2);
		canvas.drawBitmap(bitmapOrg, matrix, paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		// 存储新合成的图片
		canvas.restore();

//		saveMyBitmap(afterBitmap, String.valueOf(Calendar.getInstance().getTime().getTime()));

		return afterBitmap;
	}
	
	public static void saveMyBitmap2(Bitmap bitmap, File shipChannelCacheImg) { 
		FileOutputStream fOut = null;  
		
		try {
			fOut = new FileOutputStream(shipChannelCacheImg);  
			
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);  
			
			fOut.flush();  
			
			fOut.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
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

	// 计算幂
	public static int GetPower(int nValue, int nCount) throws Exception {
		if (nCount < 0)
			throw new Exception("nCount can't small than 1!");
		if (nCount == 0)
			return 1;
		int nSum = 1;
		for (int i = 0; i < nCount; ++i) {
			nSum = nSum * nValue;
		}
		return nSum;
	}

	// 判断是否是16进制数
	public static boolean IsHex(String strHex) {
		int i = 0;
		if (strHex.length() > 2) {
			if (strHex.charAt(0) == '0'
					&& (strHex.charAt(1) == 'X' || strHex.charAt(1) == 'x')) {
				i = 2;
			}
		}
		for (; i < strHex.length(); ++i) {
			char ch = strHex.charAt(i);
			if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F')
					|| (ch >= 'a' && ch <= 'f'))
				continue;
			return false;
		}
		return true;
	}

	// 计算16进制对应的数值
	public static int GetHex(char ch) throws Exception {
		if (ch >= '0' && ch <= '9')
			return (int) (ch - '0');
		if (ch >= 'a' && ch <= 'f')
			return (int) (ch - 'a' + 10);
		if (ch >= 'A' && ch <= 'F')
			return (int) (ch - 'A' + 10);
		throw new Exception("error param");
	}

	// 16进制转10进制
	public static int HexToInt(String strHex) {
		int nResult = 0;
		if (!IsHex(strHex))
			return nResult;
		String str = strHex.toUpperCase();
		if (str.length() > 2) {
			if (str.charAt(0) == '0' && str.charAt(1) == 'X') {
				str = str.substring(2);
			}
		}
		int nLen = str.length();
		for (int i = 0; i < nLen; ++i) {
			char ch = str.charAt(nLen - i - 1);
			try {
				nResult += (GetHex(ch) * GetPower(16, i));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return nResult;
	}

	// 10进制转16进制
	public static String IntToHex(int n) {
		char[] ch = new char[20];
		int nIndex = 0;
		while (true) {
			int m = n / 16;
			int k = n % 16;
			if (k == 15)
				ch[nIndex] = 'F';
			else if (k == 14)
				ch[nIndex] = 'E';
			else if (k == 13)
				ch[nIndex] = 'D';
			else if (k == 12)
				ch[nIndex] = 'C';
			else if (k == 11)
				ch[nIndex] = 'B';
			else if (k == 10)
				ch[nIndex] = 'A';
			else
				ch[nIndex] = (char) ('0' + k);
			nIndex++;
			if (m == 0)
				break;
			n = m;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(ch, 0, nIndex);
		sb.reverse();
		String strHex = new String("0x");
		strHex += sb.toString();
		return strHex;
	}

	public static String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	public static String decodeUnicode(final String theString) {
		char aChar;

		int len = theString.length();

		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;) {

			aChar = theString.charAt(x++);

			if (aChar == '\\') {

				aChar = theString.charAt(x++);

				if (aChar == 'u') {

					// Read the xxxx

					int value = 0;

					for (int i = 0; i < 4; i++) {

						aChar = theString.charAt(x++);

						switch (aChar) {

						case '0':

						case '1':

						case '2':

						case '3':

						case '4':

						case '5':

						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';

					else if (aChar == 'n')

						aChar = '\n';

					else if (aChar == 'f')

						aChar = '\f';

					outBuffer.append(aChar);

				}

			} else

				outBuffer.append(aChar);

		}

		return outBuffer.toString();
	}

	public static void writeFileData(String fileName, String message) {

		try {

			FileOutputStream fout = GlobalApplication.getInstance()
					.openFileOutput(fileName, Context.MODE_PRIVATE);

			byte[] bytes = message.getBytes();

			fout.write(bytes);

			fout.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static String readFileData(String fileName) {

		String res = "";

		try {

			FileInputStream fin = GlobalApplication.getInstance()
					.openFileInput(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];

			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			fin.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		return res;

	}
}

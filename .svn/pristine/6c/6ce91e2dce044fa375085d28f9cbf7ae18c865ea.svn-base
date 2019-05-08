package com.eyunda.main.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.hy.client.R;
import com.ta.TAApplication;
import com.ta.util.bitmap.TABitmapCacheWork;
import com.ta.util.bitmap.TABitmapCallBackHanlder;
import com.ta.util.bitmap.TADownloadBitmapHandler;
import com.ta.util.extend.draw.DensityUtils;

public class Image_loader {
	protected static TABitmapCacheWork imageFetcher;
	protected static TABitmapCacheWork normal_imageFetcher;
	protected static TABitmapCacheWork noloading_img;

	public Image_loader(Context context, TAApplication application) {
		if (imageFetcher == null) {
			TADownloadBitmapHandler downloadBitmapFetcher = new TADownloadBitmapHandler(
					context, DensityUtils.dipTopx(context, 128),
					DensityUtils.dipTopx(context, 128));
			TABitmapCallBackHanlder taBitmapCallBackHanlder = new TABitmapCallBackHanlder();
			taBitmapCallBackHanlder
					.setLoadingImage(context, R.drawable.loading);
			imageFetcher = new TABitmapCacheWork(context);
			imageFetcher.setProcessDataHandler(downloadBitmapFetcher);
			imageFetcher.setCallBackHandler(taBitmapCallBackHanlder);
			imageFetcher.setFileCache(application.getFileCache());
		}
		if (normal_imageFetcher == null) {
			TADownloadBitmapHandler downloadBitmapFetcher = new TADownloadBitmapHandler(
					context, DensityUtils.dipTopx(context, 128),
					DensityUtils.dipTopx(context, 128));
			TABitmapCallBackHanlder taBitmapCallBackHanlder = new TABitmapCallBackHanlder();
			taBitmapCallBackHanlder.setLoadingImage(context,
					R.drawable.zd_icon);
			normal_imageFetcher = new TABitmapCacheWork(context);
			normal_imageFetcher.setProcessDataHandler(downloadBitmapFetcher);
			normal_imageFetcher.setCallBackHandler(taBitmapCallBackHanlder);
			normal_imageFetcher.setFileCache(application.getFileCache());
		}
		if(noloading_img == null) {
			TADownloadBitmapHandler downloadBitmapFetcher = new TADownloadBitmapHandler(
					context, DensityUtils.dipTopx(context, 128),
					DensityUtils.dipTopx(context, 128));
			TABitmapCallBackHanlder taBitmapCallBackHanlder = new TABitmapCallBackHanlder();
			taBitmapCallBackHanlder.setLoadingImage(context,
					R.drawable.zd_icon);
			noloading_img = new TABitmapCacheWork(context);
			noloading_img.setProcessDataHandler(downloadBitmapFetcher);
//			noloading_img.setCallBackHandler(taBitmapCallBackHanlder);
			noloading_img.setFileCache(application.getFileCache());
		}

	}

	public void load_horizontal_Img(String url, ImageView img) {
		 if(url==null) return;
			imageFetcher.loadFormCache(encodeURL(url,"UTF-8"), img);
		 
	}

	public void load_normal_Img(String url, ImageView img) {
		 //http://115.28.227.82:9001/assets/headPortait/20140606205311186.jpg
			normal_imageFetcher.loadFormCache(encodeURL(url,"UTF-8"),
					img);
		 
	}
	
	public void load_noloading_Img(String url, ImageView img) {
		 //http://115.28.227.82:9001/assets/headPortait/20140606205311186.jpg
		Log.d("tag",url);
		noloading_img.loadFormCache(encodeURL(url,"UTF-8"),
					img);
		 
	}

	public static String encodeURL(String url, String encode)
			 {
		StringBuilder sb = new StringBuilder();
		try {
			StringBuilder noAsciiPart = new StringBuilder();
			for (int i = 0; i < url.length(); i++) {
				char c = url.charAt(i);
				if (c > 255) {
					noAsciiPart.append(c);
				} else {
					if (noAsciiPart.length() != 0) {
						sb.append(URLEncoder.encode(noAsciiPart.toString(), encode));
						noAsciiPart.delete(0, noAsciiPart.length());
					}
					sb.append(c);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}

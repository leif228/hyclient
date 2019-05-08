package com.hangyi.zd.play;

import java.io.File;

import android.os.Environment;

import com.eyunda.third.GlobalApplication;
import com.hangyi.tools.CookieImageDownloader;
import com.hangyi.tools.TrueFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;


public class PlayImageLoader {
	private static ImageLoaderConfiguration imageLoaderConfiguration= null;
	private static File SdCardPath = Environment.getExternalStorageDirectory();// 获取SDCard目录
	
	private PlayImageLoader() {
	}

	public static ImageLoaderConfiguration getInstance() {
		if (imageLoaderConfiguration == null){
			File cacheDir = new File(SdCardPath + "/zd/img");
			imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(GlobalApplication.getInstance())
//			.memoryCacheExtraOptions(200, 200)
			// 设置内存缓存的详细信息
			// max width, max height，即保存的每个缓存文件的最大长宽
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			// .discCacheExtraOptions(48, 48, null, 0, null)//设置sd卡缓存的详细信息
			// 线程池内加载的数量
			.threadPoolSize(10)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.discCache(new UnlimitedDiscCache(cacheDir))
			// 自定义缓存路径,图片缓存到sd卡
			.tasksProcessingOrder(QueueProcessingType.FIFO)
//			.memoryCache(new LruMemoryCache(4 * 1024 * 1024))
//			.memoryCacheSizePercentage(10)
			.discCacheFileCount(50000) //缓存的文件数量  
			// 超时时间 5秒
			.imageDownloader(new CookieImageDownloader(GlobalApplication.getInstance(), 3 * 1000, 3 * 1000))
			.imageDecoder(new BaseImageDecoder(true))
			.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
			.writeDebugLogs().build();// 开始构建

		}
		return imageLoaderConfiguration;
	}
}

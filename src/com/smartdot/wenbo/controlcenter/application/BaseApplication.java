package com.smartdot.wenbo.controlcenter.application;

import io.rong.imkit.RongIM;

import java.io.File;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.rong.RongCloudEvent;

public class BaseApplication extends Application {

	private static BaseApplication application = null;
	private static DbUtils dbUtils;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		// ImageLoader的配置
		final File imgCacheDir = new File(
				Environment.getExternalStorageDirectory()
						+ "/wenbohui/imageloader", "images");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).diskCache(new UnlimitedDiscCache(imgCacheDir))
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new WeakMemoryCache())
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(70 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.writeDebugLogs()
				.imageDownloader(new BaseImageDownloader(this, 20000, 20000))
				.build();
		ImageLoader.getInstance().init(config);
		// 百度地图的配置
		SDKInitializer.initialize(this);
		// 融云im的配置
		if (getApplicationInfo().packageName
				.equals(getCurProcessName(getApplicationContext()))
				|| "io.rong.push"
						.equals(getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第一步 初始化
			 */
			RongIM.init(this);

			if (getApplicationInfo().packageName
					.equals(getCurProcessName(getApplicationContext()))) {
				// RongIMClient.setOnReceiveMessageListener(new
				// MyReceiveMessageListener());
				RongCloudEvent.init(this);
				// DemoContext.init(this);
			}
		}
		//
		DaoConfig daoConfig = new DaoConfig(this);
		daoConfig.setDbName("THIS.sqlite");
		// zyj 修改数据库保存位置
		// daoConfig.setDbDir(AppTools.getRootPath() + File.separator
		// + "databases");
		daoConfig.setDbDir(Constant.databaseTarget);
		dbUtils = DbUtils.create(daoConfig);

	}

	public static BaseApplication getInstance() {
		return application;
	}

	public static String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {

				return appProcess.processName;
			}
		}
		return null;
	}
}

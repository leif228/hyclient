package com.hangyi.tools;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

public class CloseActivityClass {

	public static List<Activity> activityList = new ArrayList<Activity>();

	public static void exitClient(Context ctx) {
		// 关闭所有Activity
		for (int i = 0; i < activityList.size(); i++) {
			if (null != activityList.get(i)) {
				activityList.get(i).finish();
			}
		}
//		ActivityManager activityMgr = (ActivityManager) ctx
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		activityMgr.restartPackage(ctx.getPackageName());
//		System.exit(0);
	}
}

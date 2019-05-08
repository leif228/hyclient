package com.eyunda.tools;

import com.eyunda.tools.log.Log;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


/**
 * Created by Administrator on 2015/3/19.
 */
public class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    final String TAG_ACTIVITY_LIFE = "lifeCycle";
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i(TAG_ACTIVITY_LIFE, activity.getClass().getName() + " : onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i(TAG_ACTIVITY_LIFE, activity.getClass().getName() + " : onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i(TAG_ACTIVITY_LIFE, activity.getClass().getName() + " : onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i(TAG_ACTIVITY_LIFE, activity.getClass().getName() + " : onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i(TAG_ACTIVITY_LIFE, activity.getClass().getName() + " : onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.i(TAG_ACTIVITY_LIFE, activity.getClass().getName() + " : onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i(TAG_ACTIVITY_LIFE, activity.getClass().getName() + " : onActivityDestroyed");
    }
}

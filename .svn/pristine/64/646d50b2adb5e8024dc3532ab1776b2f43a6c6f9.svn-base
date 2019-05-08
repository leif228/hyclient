package com.eyunda.third.adapters.chat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DensityUtil
{

    public DensityUtil()
    {
    }

    public static int dip2px(Context context, float f)
    {
        float f1 = context.getResources().getDisplayMetrics().density;
        return (int)(f * f1 + 0.5F);
    }

    public static int px2dip(Context context, float f)
    {
        float f1 = context.getResources().getDisplayMetrics().density;
        return (int)(f / f1 + 0.5F);
    }

    public static int sp2px(Context context, float f)
    {
        float f1 = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(f * f1 + 0.5F);
    }

    public static int px2sp(Context context, float f)
    {
        float f1 = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(f / f1 + 0.5F);
    }
}

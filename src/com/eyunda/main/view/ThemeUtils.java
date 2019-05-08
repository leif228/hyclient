package com.eyunda.main.view;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
/***
 * UI工具类
 * @author wujiahong
 * 2013-1-22
 */
public class ThemeUtils {

/***
 * 获取当前资源包
 * @param context
 * @return
 */
public static String getPackageName(Context context){
	String pkgName = context .getPackageName();
	return pkgName;
}

/***
 * 查找资源 drawable
 * @param context
 */
public static Drawable findDrawable(Context context,String resName){
	String pkgName = getPackageName(context);
	Resources themeResources = null;
	Drawable drawable=null;
	try{
	     themeResources = context.getPackageManager().getResourcesForApplication(pkgName);
	     if(themeResources==null){
        	 pkgName = context.getPackageName();
             themeResources = context.getPackageManager().getResourcesForApplication(pkgName);
         }
        int resource_id = themeResources.getIdentifier(resName, "drawable", pkgName);
        if(resource_id!=0)
        drawable=themeResources.getDrawable(resource_id);
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return drawable;
}


/***
 * 查找String 资源
 * @param context
 * @param resName
 * @return
 */
public static String findString(Context context,String resName){
	String pkgName = getPackageName(context);
	Resources themeResources = null;
	String value="";
	try{
	     themeResources = context.getPackageManager().getResourcesForApplication(pkgName);
	     if(themeResources==null){
        	 pkgName = context.getPackageName();
             themeResources = context.getPackageManager().getResourcesForApplication(pkgName);
         }
        int resource_id = themeResources.getIdentifier(resName, "string", pkgName);
        if(resource_id!=0)
        	value=themeResources.getString(resource_id);
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return value;
}

/***
 * 换皮肤
 * @param context
 * @param view
 * @param resName
 */
public static void setWidgetTheme(Context context, View view, String resName){

	    String pkgName = getPackageName(context);

        Resources themeResources = null;

        try {

            themeResources = context.getPackageManager().getResourcesForApplication(pkgName);
            if(themeResources==null){
            	 pkgName = context.getPackageName();
                 themeResources = context.getPackageManager().getResourcesForApplication(pkgName);
            }

        } catch (NameNotFoundException e) {

            e.printStackTrace();

        }

        if(themeResources == null){

            return;

        }
     
       try {
            int resource_id = themeResources.getIdentifier(resName, "drawable", pkgName);
            if(resource_id != 0){

                Drawable drawable = themeResources.getDrawable(resource_id);

                if(view != null){

                    view.setBackgroundDrawable(drawable);

                }

            }

        } catch(Resources.NotFoundException e){

            e.printStackTrace();

        }

    }

 /***
  * 获取资源ID
  * @param context
  * @param resName:loading,icon,main...
  * @param resType:layout,drawable,String..
  * @return
  */
 public static int getResourceId(Context context,String resName,String resType){
	 String pkgName = getPackageName(context);
	 Resources themeResources = null;
	 int resource_id=0;
	 try {

         themeResources = context.getPackageManager().getResourcesForApplication(pkgName);
         if(themeResources==null){
         	 pkgName = context.getPackageName();
             themeResources = context.getPackageManager().getResourcesForApplication(pkgName);
         }
         resource_id = themeResources.getIdentifier(resName, resType, pkgName);

     } catch (Exception e) {

        e.printStackTrace();

     }
	return resource_id; 
 }
 
 /***
  * 在R类下获取具体资源ID 
  * @param c
  * @param fieldName
  * @return
  */
 public static int findResClassId(Context mc,String clssType,String fieldName){
	 int resId=0;
	 try{
		 Class c=Class.forName(ThemeUtils.getPackageName(mc)+".R$"+clssType);
	     Field field=c.getField(fieldName);
	     resId=field.getInt(null);
	 }catch(Exception e){
		 e.printStackTrace();
	 }
	 return resId;
 }
}

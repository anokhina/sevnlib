/*
 * Copyright 2016 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.org.sevn.alib.util;

import java.util.Collections;
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;

public class AppsUtil {
    public static Intent getAppIntent(final Context ctx, final String packageName) {
        if (packageName == null) return null;
        PackageManager pm = ctx.getPackageManager();
        
        Intent i = pm.getLaunchIntentForPackage(packageName);
        if (i == null) {
            Intent intent = new Intent();
            intent.setPackage(packageName);

            List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
            Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

            if(resolveInfos.size() > 0) {
                ResolveInfo launchable = resolveInfos.get(0);
                ActivityInfo activity = launchable.activityInfo;
                ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                        activity.name);
                i=new Intent(Intent.ACTION_MAIN);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                i.setComponent(name);
            }               
        }
        return i;
    }
    
    public static Intent runApp(final Context ctx, final String packageName) {
        Intent i = getAppIntent(ctx, packageName);
        if (i != null) {
            ctx.startActivity(i);
        }
        return i;
    }
    
    public static Resources getAppResources(Context ctx, String packageName) {
        try {
            return ctx.getPackageManager().getResourcesForApplication(packageName);
        } catch (NameNotFoundException e) {
            return null;
        }
    }
    
    public static List<ResolveInfo> getAvailableActivities(final Context ctx) {
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager manager = ctx.getPackageManager();
        return manager.queryIntentActivities(i, 0);
    }
    public static List<ResolveInfo> getAvailableReferences(final Context ctx) {
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i = new Intent(Intent.ACTION_CREATE_SHORTCUT);
        PackageManager manager = ctx.getPackageManager();
        return manager.queryIntentActivities(i, 0);
    }
    public static List<AppWidgetProviderInfo> getInstalledAppWidgetList(Context ctx) {
        AppWidgetManager manager;
        manager = AppWidgetManager.getInstance(ctx);
        return manager.getInstalledProviders();
    }
    public static void showHome(Context ctx) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(startMain);        
    }
    public static void showHomeAny(Context ctx) {
        
        PackageManager pm = ctx.getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> lst = pm.queryIntentActivities(i, 0);
        //ToastUtil.rtoastLong(ctx, "---startActivity---"+lst);
        if (lst != null) {
           for (ResolveInfo resolveInfo : lst) {
               try {
                   Intent home = new Intent(Intent.ACTION_MAIN);
                   home.addCategory(Intent.CATEGORY_HOME);
                   home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   home.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                   //ToastUtil.rtoastLong(ctx, "---startActivity---"+lst.size()+":"+resolveInfo.activityInfo.packageName);
                   ctx.startActivity(home);
                   break;
               } catch (Throwable t) {
                   t.printStackTrace();
               }
           }
        }        
    }
    
}

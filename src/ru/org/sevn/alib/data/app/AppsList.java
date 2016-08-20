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
package ru.org.sevn.alib.data.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.org.sevn.alib.data.app.AppDetail;
import ru.org.sevn.alib.data.app.AppDetailComparator;
import ru.org.sevn.alib.data.app.AppDetailManager;
import ru.org.sevn.alib.util.AppsUtil;
import ru.org.sevn.alib.util.CollectionsUtil;
import android.content.Context;

public class AppsList {
    private List<AppDetail> allApps = new ArrayList<>();
    private HashMap<String, AppDetail> map = new HashMap<>();
    
    public List<AppDetail> getAppsInfo(final Context ctx){
        return getAppsInfo(ctx, new AppDetailComparator());
    }
    public List<AppDetail> getAppsInfo(final Context ctx, final AppDetailComparator cmpr){
        List<AppDetail> apps = new ArrayList<AppDetail>();
        getAllApps(ctx, cmpr);
        synchronized (allApps) {
            CollectionsUtil.copy(apps, allApps);
        }
        return apps;
    }   
    public void clearCache() {
        synchronized (allApps) {
            allApps.clear();
        }
    }
    public AppDetail findApp(String p, final Context ctx, final AppDetailComparator cmpr) {
        getAllApps(ctx, cmpr);
        synchronized (map) {
            return map.get(p);
        }
    }
    private List<AppDetail> getAllApps(final Context ctx, final AppDetailComparator cmpr) {
        boolean rebuildMap = false;
        
        synchronized (allApps) {
            if (allApps.size() == 0) {
                rebuildMap = true;
                allApps = AppDetailManager.makeAppDetailList(ctx, AppsUtil.getAvailableActivities(ctx), cmpr);
            }
        }
        if (rebuildMap) {
            synchronized (allApps) {
                synchronized (map) {
                    map.clear();
                    for(AppDetail ad : allApps) {
                        map.put(ad.getPackageName(), ad);
                    }
                }
            }
        }
        return allApps;
    }
    
}
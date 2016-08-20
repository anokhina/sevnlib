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

import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

public class DrawableUtil {
    
    public static Drawable getDrawableForDensityMax(Resources resources, int iconid) {
        if (resources == null) return null;
        try {
            int density = resources.getDisplayMetrics().densityDpi;
            if (DisplayMetrics.DENSITY_XHIGH > density) {
                density = DisplayMetrics.DENSITY_XHIGH;
            }
            return resources.getDrawableForDensity(iconid, density);
        } catch (Exception e) { 
            return resources.getDrawable(iconid);
        }
    }
    
    public static Drawable getIconDrawable(PackageManager manager, PackageItemInfo pii) {
        Drawable appIcon = null;
        try {
            Resources resourcesForApplication = manager.getResourcesForApplication(pii.packageName);
            
            appIcon = getDrawableForDensityMax(resourcesForApplication, pii.icon);
        } catch (Exception e) {
            Log.e("check", "error getting Hi Res Icon :", e);
        }
        if (appIcon == null) {
            appIcon = pii.loadIcon(manager);
        }
        return appIcon;
    }
}

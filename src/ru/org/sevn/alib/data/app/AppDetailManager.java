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
import java.util.Collections;
import java.util.List;

import ru.org.sevn.alib.util.DrawableUtil;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class AppDetailManager {
    public static List<AppDetail> makeAppDetailList(final Context ctx, final List<ResolveInfo> rilst, final AppDetailComparator cmpr) {
        List<AppDetail> ret = new ArrayList<>();
        for (ResolveInfo ri : rilst) {
            ret.add(makeAppDetail(ctx, ri));
        }
        if (cmpr != null) {
            Collections.sort(ret, cmpr);
        }
        return ret;
    }
    public static AppDetail makeAppDetail(final Context ctx, final ResolveInfo ri) {
        PackageManager manager = ctx.getPackageManager();
        AppDetail app = new AppDetail();
        app.getExtra().setLabel(ri.loadLabel(manager).toString());
        app.setPackageName(ri.activityInfo.packageName);
        //ri.activityInfo.applicationInfo.sourceDir;
        //ri.activityInfo.applicationInfo.publicSourceDir;
        //ri.activityInfo.applicationInfo.loadIcon(pm);
        
        app.getExtra().setIcon(DrawableUtil.getIconDrawable(manager, ri.activityInfo));
        return app;     
    }
}

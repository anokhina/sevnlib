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

import ru.org.sevn.alib.data.Copyable;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class AppDetailExtra implements Copyable<AppDetailExtra>{
    private Drawable icon;
    private ResolveInfo info;
    private String label;
    
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public ResolveInfo getInfo() {
        return info;
    }
    public void setInfo(ResolveInfo info) {
        this.info = info;
    }
    
    @Override
    public AppDetailExtra copy() {
        return new AppDetailExtra().copyFrom(this);
    }
    
    @Override
    public AppDetailExtra copyFrom(AppDetailExtra obj) {
        if (obj != null) {
            icon = obj.icon;
            info = obj.info;
            label = obj.label;
        }
        return this;
    }

}

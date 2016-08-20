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
import ru.org.sevn.alib.gui.HasIcon;
import android.graphics.drawable.Drawable;

public class AppDetail implements HasIcon, Copyable<AppDetail> {
	private long id;
	private int sortOrder;
	private String packageName;
	
	private transient final AppDetailExtra extra = new AppDetailExtra();
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String name) {
		this.packageName = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String toString() {
	    return getExtra().getLabel();
	}
    @Override
    public Object getKey() {
        return getPackageName();
    }
    
    public AppDetailExtra getExtra() {
        return extra;
    }
    public AppDetail copy() {
        return new AppDetail().copyFrom(this);
    }
    @Override
    public AppDetail copyFrom(AppDetail obj) {
        if (obj != null) {
            id = obj.id;
            packageName = obj.packageName;
            sortOrder = obj.sortOrder;
            extra.copyFrom(obj.extra);
        }
        return this;
    }
    @Override
    public Drawable getIcon() {
        return getExtra().getIcon();
    }
}

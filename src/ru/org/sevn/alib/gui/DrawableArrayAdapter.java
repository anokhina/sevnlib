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
package ru.org.sevn.alib.gui;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DrawableArrayAdapter extends ArrayAdapter<HasIcon> {

    private HashMap<Object, HasIcon> map = new HashMap();
    
    public DrawableArrayAdapter(Context context, int resource,
            int textViewResourceId, List<HasIcon> objects) {
        super(context, resource, textViewResourceId, objects);
        this.rebuildMap();
    }
    
    private void rebuildMap() {
        map.clear();
        for (int i = getCount() - 1; i >=0; i-- ) {
            HasIcon obj = getItem(i);
            map.put(obj.getKey(), obj);
        }
    }
    
    public boolean contains(HasIcon obj) {
        return map.containsKey(obj.getKey());
    }
    
    public boolean containsKey(Object obj) {
        return map.containsKey(obj);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = super.getView(position, convertView, parent);
        return alterIcon(ret, position, convertView, parent);
    }
    
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View ret = super.getDropDownView(position, convertView, parent);
        return alterIcon(ret, position, convertView, parent);
    }
    
    protected View alterIcon(View v, int position, View convertView, ViewGroup parent) {
        HasIcon item = getItem(position);
        
        TextView button = (TextView)v;
        // Left, top, right, bottom drawables.
        Drawable[] drawables = button.getCompoundDrawables();
        Drawable leftCompoundDrawable = drawables[0];
        Drawable img = getItemDrawable(item);
        img.setBounds(leftCompoundDrawable.getBounds());
        button.setCompoundDrawables(img, null, null, null);
        return v;
    }
    
    protected Drawable getItemDrawable(HasIcon item) {
        return item.getIcon();
    }
    
    @Override
    public void notifyDataSetChanged() {
        rebuildMap();
        super.notifyDataSetChanged();
    }    
}

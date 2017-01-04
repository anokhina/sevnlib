/*******************************************************************************
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
 *******************************************************************************/
package ru.org.sevn.alib.gui;

import java.util.Collection;

import android.widget.ListAdapter;

public interface AppListViewAdapter<T> extends ListAdapter {
	//void notifyDataSetChanged();
	void updateList();

	void addAll(Collection<? extends T> queryForAll) throws UpdateException;

	void add(Collection<T> obj) throws UpdateException;

	void remove(Collection<T> obj) throws UpdateException;
	
	void update(Collection<T> obj) throws UpdateException;
	
    T getObjectItem(int position);
	
	public static class UpdateException extends Exception {
		public UpdateException(String msg) {
			super(msg);
		}
		public UpdateException(String msg, Throwable tr) {
			super(msg, tr);
		}
		public UpdateException(Throwable tr) {
			super(tr);
		}
	}
}

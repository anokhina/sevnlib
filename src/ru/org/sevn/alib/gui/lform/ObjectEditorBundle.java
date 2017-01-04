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
package ru.org.sevn.alib.gui.lform;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

public class ObjectEditorBundle<V extends View, T> {
	private HideView editorView;
	private V objectView;
	private List<T> edited;
	public ObjectEditorBundle(V v) {
		setObjectView(v);
	}
	public HideView getEditorView() {
		return editorView;
	}
	public void setEditorView(HideView alertDialog) {
		this.editorView = alertDialog;
	}
	public V getObjectView() {
		return objectView;
	}
	public void setObjectView(V view) {
		this.objectView = view;
	}
	public List<T> getEdited() {
		return edited;
	}
	public void setFirstEdited(T edited) {
		this.edited = new ArrayList<>();
		this.edited.add(edited);
	}
	public void setEdited(List<T> edited) {
		this.edited = edited;
	}
	public T getFirstEdited() {
		if (edited != null && edited.size() > 0) {
			return edited.get(0);
		}
		return null;
	}
}
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

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;

public class AnyHideView<T> extends HideView<T> {
	public AnyHideView(T view, ViewGroup vp) {
		super(view, vp);
		if (view instanceof Dialog || view instanceof View) { } else {
			throw new IllegalArgumentException("Parameter to be Dialog or View");
		}
	}
	public void show() {
		if (getComponent() instanceof Dialog) {
			((Dialog) getComponent()).show();
		}
		if (getComponent() instanceof View) {
			View v = (View)getComponent();
			if (v.getVisibility() == View.VISIBLE) {
				v.setVisibility(View.GONE);
			} else {
				v.setVisibility(View.VISIBLE);
			}
		}
	}
}
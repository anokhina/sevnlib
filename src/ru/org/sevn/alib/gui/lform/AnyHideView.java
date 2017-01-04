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
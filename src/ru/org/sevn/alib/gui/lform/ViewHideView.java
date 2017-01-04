package ru.org.sevn.alib.gui.lform;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ViewHideView<T extends View> extends HideView<T> {

	public ViewHideView(T view, ViewGroup vp) {
		super(view, vp);
	}

	@Override
	public void show() {
		setVisible(true);
	}
	public void hide() {
		setVisible(false);
	}
	private void setVisible(boolean b) {
		View v = getComponent();
		if (v.getParent() == null) {
			if (b) {
				if (getViewParent() instanceof FrameLayout) {
					FrameLayout fl = (FrameLayout)getViewParent();
					fl.removeAllViews();
				}
				getViewParent().addView(v);
			}
		} else {
			if (!b) {
				getViewParent().removeView(v);
			}
		}
		
		/*
		if (v.getVisibility() == View.VISIBLE) {
			if (!b) {
				v.setVisibility(View.GONE);
			}
		} else {
			if (b) {
				v.setVisibility(View.VISIBLE);
			}
		}
		*/
	}
}
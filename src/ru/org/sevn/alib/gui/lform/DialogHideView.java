package ru.org.sevn.alib.gui.lform;

import android.app.Dialog;
import android.view.ViewGroup;

public class DialogHideView<T extends Dialog> extends HideView<T> {

	public DialogHideView(T view) {
		super(view, null);
	}

	@Override
	public void show() {
		Dialog v = getComponent();
		v.show();
	}
}
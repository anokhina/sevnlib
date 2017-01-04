package ru.org.sevn.alib.gui.lform;

import android.view.ViewGroup;

public abstract class HideView<T> {
	private final T view;
	private final ViewGroup viewParent;
	public HideView(T view, ViewGroup viewParent) {
		this.view = view;
		this.viewParent = viewParent;
	}
	public abstract void show();
	public void hide() {}
	public T getComponent() {
		return view;
	}
	public ViewGroup getViewParent() {
		return viewParent;
	}
}
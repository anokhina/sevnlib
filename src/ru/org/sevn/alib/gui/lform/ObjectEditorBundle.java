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
package ru.org.sevn.alib.gui.lform;

public class EditorConfigInfo {
	public int layoutEdit;
	public int layoutEditContent;
	public int btnEditOk;
	public int btnEditCancel;
	public int editor_view;
	private boolean useEditor;
	public int parent_container;
	public boolean isUseEditor() {
		return useEditor;
	}
	public void setUseEditor(boolean useEditor) {
		this.useEditor = useEditor;
	}
}
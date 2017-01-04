package ru.org.sevn.alib.gui.lform.action;

import java.util.List;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import ru.org.sevn.alib.gui.lform.ObjectEditorBundle;

public interface ObjectAction<T> {
	void inflateMenu(MenuInflater mi, Menu menu);
	int getActionId();
	void doActionList(List<T> selectedItems);
	void doAction(T selectedItem);
	boolean isMultiple();
	ObjectEditorBundle<View, T> getObjectEditorBundle();
}

package ru.org.sevn.alib.gui.lform.action;

import java.util.Arrays;

import ru.org.sevn.alib.gui.AppListViewAdapter.UpdateException;
import ru.org.sevn.alib.gui.lform.ListFormFragment;

public abstract class AddObjectAction<T> extends EditObjectAction<T>{

	protected AddObjectAction(ListFormFragment<T> context, int actionId, int titleId, int messageId) {
		super(context, actionId, titleId, messageId);
	}

	protected T getEdited() {
		return null;
	}
	
	protected void update(T obj) throws UpdateException{
		getListForm().getAppListViewAdapter().add(Arrays.asList(obj));
	}
	
}

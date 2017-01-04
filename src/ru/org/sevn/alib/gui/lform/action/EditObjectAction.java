package ru.org.sevn.alib.gui.lform.action;

import java.util.Arrays;
import java.util.List;

import android.view.View;
import ru.org.sevn.alib.gui.AppListViewAdapter.UpdateException;
import ru.org.sevn.alib.gui.lform.ListFormFragment;

public abstract class EditObjectAction<T> extends AbstractEditObjectAction<T>{

	protected EditObjectAction(ListFormFragment<T> context, int actionId, int titleId, int messageId) {
		super(context, actionId, false, titleId, messageId);
	}

	@Override
	protected void setEditViewMultiple(List<T> selectedItems) {
	}
	
	protected abstract T view2object(View v, T obj);
	
	protected T getEdited() {
		return getObjectEditorBundle().getFirstEdited();
	}
	
	@Override
	protected void processOkAction() throws UpdateException{
		T obj = view2object(getObjectEditorBundle().getObjectView(), getEdited());
        if (obj != null) {
        	update(obj);
        }
	}

	protected void update(T obj) throws UpdateException{
		getListForm().getAppListViewAdapter().update(Arrays.asList(obj));
	}
}

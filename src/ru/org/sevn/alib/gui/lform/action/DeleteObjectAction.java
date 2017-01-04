package ru.org.sevn.alib.gui.lform.action;

import java.util.List;

import android.view.View;
import android.widget.EditText;
import ru.org.sevn.alib.data.Object2String;
import ru.org.sevn.alib.gui.AppListViewAdapter.UpdateException;
import ru.org.sevn.alib.gui.lform.ListFormFragment;

public abstract class DeleteObjectAction<T> extends AbstractEditObjectAction<T>{

	protected DeleteObjectAction(Object2String<T> object2String, ListFormFragment<T> context, int actionId, int titleId, int messageId) {
		super(context, actionId, true, titleId, messageId);
		this.object2String = object2String; 
	}

	final Object2String<T> object2String;
	
	@Override
	protected View makeDialogView() {
        EditText inputText = new EditText(getListForm().getActivity());
        return inputText;
	}

	@Override
	protected void setEditView(T selectedItem) {
	}

	@Override
	protected void setEditViewMultiple(List<T> selectedItems) {
    	((EditText)getObjectEditorBundle().getObjectView()).setText(makeDelMsg(selectedItems));
	}
	
	protected String makeStringQuestion(int size) {
		return "Delete " + size + " objects?\n";
	}
	
	protected String makeDelMsg(List<T> objs) {
		String msg = "";
		if (objs != null && objs.size() > 0) {
			msg += makeStringQuestion(objs.size());
			for (int i = 0; i < 5 && i < objs.size(); i++) {
				T obj = objs.get(i);
				msg += object2String.getStringRepresentation(obj) + "\n";
			}
		}
		return msg;
	}
	
	@Override
	protected void processOkAction() throws UpdateException{
		List<T> lst = getObjectEditorBundle().getEdited();
		if (lst != null) {
			getListForm().getAppListViewAdapter().remove(lst);
		}
	}

	
}

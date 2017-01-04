/*******************************************************************************
 * Copyright 2016 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ru.org.sevn.alib.gui.lform.action;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import ru.org.sevn.alib.gui.AppListViewAdapter.UpdateException;
import ru.org.sevn.alib.gui.lform.DialogHideView;
import ru.org.sevn.alib.gui.lform.EditorConfigInfo;
import ru.org.sevn.alib.gui.lform.ListFormFragment;
import ru.org.sevn.alib.gui.lform.ObjectEditorBundle;
import ru.org.sevn.alib.gui.lform.ViewHideView;

public abstract class AbstractEditObjectAction<T> implements ObjectAction<T>{

	private ObjectEditorBundle<View, T> objectEditorBundle;
	
	private final int actionId;
	private final boolean multiple;
	private final int titleId;
	private final int messageId;
	private final ListFormFragment<T> listForm;
	
	protected AbstractEditObjectAction(ListFormFragment<T> context, int actionId, boolean multiple, int titleId, int messageId) {
		this.listForm = context;
		this.actionId = actionId;
		this.multiple = multiple;
		this.titleId = titleId;
		this.messageId = messageId;
	}

	@Override
	public int getActionId() {
		return actionId;
	}

	@Override
	public boolean isMultiple() {
		return multiple;
	}

	protected ListFormFragment<T> getListForm() {
		return listForm;
	}

	protected int getTitleId() {
		return titleId;
	}

	protected int getMessageId() {
		return messageId;
	}

	@Override
	public void doActionList(List<T> selectedItems) {
		ObjectEditorBundle<View, T> bundle = getObjectEditorBundle();
		bundle.setEdited(selectedItems);
		setEditViewMultiple(selectedItems);
		bundle.getEditorView().show();
	}

	@Override
	public void doAction(T selectedItem) {
		ObjectEditorBundle<View, T> bundle = getObjectEditorBundle();
		bundle.setFirstEdited(selectedItem);
    	setEditView(selectedItem);
		bundle.getEditorView().show();
	}
	
	protected abstract void setEditView(T selectedItem);
	protected abstract void setEditViewMultiple(List<T> selectedItems);

	@Override
	public ObjectEditorBundle<View, T> getObjectEditorBundle() {
		if (objectEditorBundle == null) {
			objectEditorBundle = makeObjectEditorBundle();
		}
		return objectEditorBundle;
	}

    protected abstract View makeDialogView();
	
	protected ObjectEditorBundle<View, T> makeObjectEditorBundle() {
        ObjectEditorBundle ret = new ObjectEditorBundle(makeDialogView());
        if (!makeViewEditor(ret, getTitleId(), getMessageId())) {
        	makeDialogEditor(ret, getTitleId(), getMessageId());
        }
    	return ret;
	}
	
	private final OnClickListener cancelOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
        	try {
        		doOnEditCancel();
        	} catch (Exception e) {}
		}
	};
	private final OnClickListener okOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
        	try {
        		doOnEditOk();
        	} catch (Exception e) {}
		}
	};
	
	protected boolean makeViewEditor(final ObjectEditorBundle dbundle, int titleId, int msgId) {
		try {
			if (getEditorConfigInfo() != null && getEditorConfigInfo().isUseEditor()) {
				ViewGroup parentContainer = (ViewGroup)getListForm().getActivity().findViewById(getEditorConfigInfo().parent_container);
				if (parentContainer != null) {
					
					LinearLayout layoutParent = (LinearLayout)getListForm().getLayoutInflater().inflate(getEditorConfigInfo().editor_view, null);
					
			    	ScrollView layout = (ScrollView)layoutParent.findViewById(getEditorConfigInfo().layoutEditContent);
			    	layout.addView(dbundle.getObjectView());
			    	
			    	dbundle.setEditorView(new ViewHideView<View>(layoutParent, parentContainer));
			    	
		            Button cancel = (Button)layoutParent.findViewById(getEditorConfigInfo().btnEditCancel);
		            cancel.setOnClickListener(cancelOnClickListener);
		            
		            Button ok = (Button)layoutParent.findViewById(getEditorConfigInfo().btnEditOk);
		            ok.setOnClickListener(okOnClickListener);
		    	
		            return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }
    
    protected boolean makeDialogEditor(ObjectEditorBundle ret, int titleId, int msgId) {
    	AlertDialog.Builder builder = makeInputDialog(getListForm().getActivity(), ret);
    	if (titleId != 0) { builder.setTitle(titleId); }
    	if (msgId != 0) { builder.setMessage(msgId); }
    	ret.setEditorView(new DialogHideView(builder.create()));
        prepareDialogTop((Dialog)ret.getEditorView().getComponent());
    	return true;
    }	
	
	private static void prepareDialogTop(Dialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
//        wmlp.x = 100;   //x position
//        wmlp.y = 100;   //y position
    }
	
    private AlertDialog.Builder makeInputDialog(Context ctx, final ObjectEditorBundle dbundle) {

        return new AlertDialog.Builder(ctx)
                .setView(dbundle.getObjectView())
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int whichButton) {
                            	try {
                            		doOnEditOk();
                            	} catch (Exception e) {}
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int whichButton) {
                            	try {
                            		doOnEditCancel();
                            	} catch (Exception e) {}
                                dialog.cancel();
                            }
                        });      
    }
    
	protected void doOnEditCancel() {
    	getObjectEditorBundle().getEditorView().hide();
	}
	
	protected void doOnEditOk() {
		try {
			processOkAction();
		} catch (UpdateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	getObjectEditorBundle().getEditorView().hide();
	}

	protected abstract void processOkAction() throws UpdateException;
	
	private EditorConfigInfo editorConfigInfo;
	
    public final EditorConfigInfo getEditorConfigInfo() {
    	if (editorConfigInfo == null) {
    		editorConfigInfo = makeEditorConfigInfo();
    	}
		return editorConfigInfo;
	}

	protected EditorConfigInfo makeEditorConfigInfo() {
		return null;
	}

}

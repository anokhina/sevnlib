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
package ru.org.sevn.alib.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import ru.org.sevn.alib.data.ObjStore;
import ru.org.sevn.alib.data.ObjStore.ObjStoreException;
import android.widget.ListView;

@Deprecated
public abstract class EditableListActivity<T> extends ListActivity implements OnItemLongClickListener {
	private final String LOG_TAG = getClass().getSimpleName();

	private AppListViewAdapter<T> adapter;
	private ObjStore<T> objStore;
	
	public static final int ACT_ADD = 1;
	public static final int ACT_DEL = 2;
	public static final int ACT_EDIT = 3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        adapter = makeAdapter();
        objStore = makeObjStore();
        fillAdapterData(adapter);
        setListAdapter(adapter);
	}
	
	protected AppListViewAdapter<T> getAppListViewAdapter() {
		return adapter;
	}
	
    @Override
    public void onContentChanged() {
    	super.onContentChanged();

    	init();
    }
    
    private void init() {
        getListView().setOnItemClickListener(mOnClickListener);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(this);
    }
	
	public ObjStore<T> getObjStore() {
		return objStore;
	}


	protected abstract ObjStore<T> makeObjStore();

	protected abstract AppListViewAdapter<T> makeAdapter();
	
	protected abstract void fillAdapterData(AppListViewAdapter<T> adapter);
	
	protected void onResume() {
		super.onResume();
	}
	
	protected abstract void inflateMenu(Menu menu);
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater();
		inflateMenu(menu);
		return true;
	}

	protected abstract int getActionId(MenuItem item);

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = getActionId(item);
		switch(id) {
		case ACT_ADD:
            showNewDialog();
            return true;
		case ACT_DEL:
            showDelDialog(getListSelectedObjects());
			return true;
		case ACT_EDIT:
			editSelected(getListSelected());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected T getListSelected() {
		SparseBooleanArray checkedItems = getListView().getCheckedItemPositions();
		if (checkedItems != null) {
		    for (int i=0; i<checkedItems.size(); i++) {
		        if (checkedItems.valueAt(i)) {
		        	return getAppListViewAdapter().getObjectItem(checkedItems.keyAt(i));
		        }
		    }
		}
		return null;
	}
	
	protected ArrayList<T> getListSelectedObjects() {
		ArrayList<T> ret = new ArrayList<>();
		SparseBooleanArray checkedItems = getListView().getCheckedItemPositions();
		if (checkedItems != null) {
		    for (int i=0; i<checkedItems.size(); i++) {
		        if (checkedItems.valueAt(i)) {
		        	ret.add(getAppListViewAdapter().getObjectItem(checkedItems.keyAt(i)));
		        }
		    }
		}
		return ret;
	}

	protected String makeDelMsg(List<T> objs) {
		String msg = "";
		if (objs != null && objs.size() > 0) {
			msg += "Delete " + objs.size() + " objects?\n";
			for (int i = 0; i < 5 && i < objs.size(); i++) {
				T obj = objs.get(i);
				msg += getObjStringRepresentation(obj) + "\n";
			}
		}
		return msg;
	}
	
	protected abstract String getObjStringRepresentation(T obj);
	
	protected abstract void setDelView(DialogBundle v, List<T> selectedItem);

	protected void showDelDialog(List<T> selectedItem) {
		if (selectedItem != null) {
	    	if (dialogDel == null) {
	            dialogDel = makeDialogBundle(OP_DEL);
	    	}    	
	    	dialogDel.setEdited(selectedItem);
	    	setDelView(dialogDel, selectedItem);
	    	dialogDel.getView().setEnabled(false);
	    	dialogDel.getAlertDialog().show();        
		}		
	}

	protected abstract void setEditView(DialogBundle v, T selectedItem);
	
	protected void editSelected(T selectedItem) {
		if (selectedItem != null) {
	    	if (dialogEdit == null) {
	            dialogEdit = makeDialogBundle(OP_EDIT);
	    	}    	
	    	dialogEdit.setFirstEdited(selectedItem);
	    	setEditView(dialogEdit, selectedItem);
	    	dialogEdit.getAlertDialog().show();        
		}		
	}

	public static class HideView<T> {
		private final T view;
		public HideView(T view) {
			this.view = view;
			if (view instanceof Dialog || view instanceof View) { } else {
				throw new IllegalArgumentException("Parameter to be Dialog or View");
			}
		}
		public void show() {
			if (view instanceof Dialog) {
				((Dialog) view).show();
			}
			if (view instanceof View) {
				View v = (View)view;
				if (v.getVisibility() == View.VISIBLE) {
					v.setVisibility(View.GONE);
				} else {
					v.setVisibility(View.VISIBLE);
				}
			}
		}
		public T getComponent() {
			return view;
		}
	}
	public static class DialogBundle<V extends View, T> {
		private HideView alertDialog;
		private V view;
		private List<T> edited;
		public DialogBundle(V v) {
			setView(v);
		}
		public HideView getAlertDialog() {
			return alertDialog;
		}
		public void setAlertDialog(HideView alertDialog) {
			this.alertDialog = alertDialog;
		}
		public V getView() {
			return view;
		}
		public void setView(V view) {
			this.view = view;
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
	
	private DialogBundle dialogNew;
	private DialogBundle dialogDel;
	private DialogBundle dialogEdit;
	
	
	
    public AppListViewAdapter<T> getAdapter() {
		return adapter;
	}

	public DialogBundle getDialogNew() {
		return dialogNew;
	}

	public DialogBundle getDialogDel() {
		return dialogDel;
	}

	public DialogBundle getDialogEdit() {
		return dialogEdit;
	}

	private static void prepareDialogTop(Dialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
//        wmlp.x = 100;   //x position
//        wmlp.y = 100;   //y position
    }
    
    protected void showNewDialog() {
    	if (dialogNew == null) {
            dialogNew = makeDialogBundle(OP_ADD);
    	}    	
    	setEditViewNew(dialogNew);
    	dialogNew.getAlertDialog().show();
    }
    
	protected void setEditViewNew(DialogBundle v) {
	}
    
    protected abstract View makeDialogView(final int op);
    
    protected abstract DialogBundle makeDialogBundle(final int op);
    
    protected DialogBundle makeDialogBundle(final int op, int titleId, int msgId) {
        DialogBundle ret = new DialogBundle(makeDialogView(op));
        if (!makeViewEditor(ret, op, titleId, msgId)) {
        	makeDialogEditor(ret, op, titleId, msgId);
        }
    	return ret;
    }
    protected boolean makeViewEditor(DialogBundle ret, final int op, int titleId, int msgId) {
    	return false;
    }
    protected boolean makeDialogEditor(DialogBundle ret, final int op, int titleId, int msgId) {
    	AlertDialog.Builder builder = makeInputDialog(op, this, ret);
    	builder.setTitle(titleId);
    	builder.setMessage(msgId);
    	ret.setAlertDialog(new HideView<Dialog>(builder.create()));
        prepareDialogTop((Dialog)ret.getAlertDialog().getComponent());
    	return true;
    }
    private AlertDialog.Builder makeInputDialog(final int op, Context ctx, final DialogBundle dbundle) {

        return new AlertDialog.Builder(ctx)
                .setView(dbundle.getView())
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int whichButton) {
                            	try {
                            		doOnEditOk(op, dbundle);
                            	} catch (Exception e) {}
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int whichButton) {
                            	try {
                            		doOnEditCancel(op, dbundle);
                            	} catch (Exception e) {}
                                dialog.cancel();
                            }
                        });      
    }
    
    public static final int OP_ADD = 1;
    public static final int OP_EDIT = 2;
    public static final int OP_DEL = 3;
    
	protected void doOnEditCancel(int op, DialogBundle<? extends View, T> dbundle) {
		
	}
	protected void doOnEditOk(int op, DialogBundle<? extends View, T> dbundle) {
		switch(op) {
		case OP_ADD:
		{
			T obj = view2object(op, dbundle.getView(), null);
            if (obj != null) {
                create(obj);
            }
		}
            break;
		case OP_EDIT:
		{
			T obj = view2object(op, dbundle.getView(), dbundle.getFirstEdited());
            if (obj != null) {
            	update(obj);
            }
		}
			break;
		case OP_DEL:
			removeSelected(dbundle.getEdited());
			break;
		}
	}
	
	protected abstract T view2object(int op, View v, T obj);
	
	private void update(T pobj) {
    	try {
    		objStore.update(pobj);
    		getAppListViewAdapter().updateList();
		} catch (ObjStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void create(T obj) {
		try {
			objStore.add(obj);
			getAppListViewAdapter().add(Arrays.asList(obj));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void removeSelected(List<T> objs) {
		if (objs != null) {
			for (T obj : objs) {
				try {
					objStore.remove(obj);
					getAppListViewAdapter().remove(Arrays.asList(obj));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    }

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//    	new Exception().printStackTrace();
//    	Toast.makeText(this, "uuuuuuuuu", Toast.LENGTH_LONG).show();
    	getListView().setItemChecked(position, true);
		return true;
	}
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
        {
            onListItemClick((ListView)parent, v, position, id);
        }
    };
    
	//TODO orientation - save state
	// initialize on resume
    
}

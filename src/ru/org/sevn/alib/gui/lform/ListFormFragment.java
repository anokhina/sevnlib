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
package ru.org.sevn.alib.gui.lform;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import ru.org.sevn.alib.data.ObjStore;
import ru.org.sevn.alib.gui.AppListViewAdapter;
import ru.org.sevn.alib.gui.lform.action.ObjectAction;

public abstract class ListFormFragment<T> extends ListFragment implements OnItemLongClickListener {
	private final String LOG_TAG = getClass().getSimpleName();

	private AppListViewAdapter<T> adapter;
	private ObjStore<T> objStore;
	private List<ObjectAction<T>> objectActionList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        objStore = makeObjStore();
        adapter = makeAdapter(objStore);
        fillAdapterData(adapter);
        setListAdapter(adapter);
        objectActionList = makeActions(savedInstanceState);
	}
	
	protected abstract List<ObjectAction<T>> makeActions(Bundle savedInstanceState);
	
	public List<ObjectAction<T>> getObjectActionList() {
		return objectActionList;
	}
	
	protected void inflateMenu(Menu menu, MenuInflater inflater) {
		if (getObjectActionList() != null) {
			for (ObjectAction a : getObjectActionList()) {
				a.inflateMenu(inflater, menu);
			}
		}
	}

	public AppListViewAdapter<T> getAppListViewAdapter() {
		return adapter;
	}
	
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    	init();
    }
    
    private void init() {
        getListView().setOnItemClickListener(mOnClickListener);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(this);
        //inflateEditorArea(getEditorId());
    }
    
    protected int getEditorId() {
    	return 0;
    }
    protected void inflateEditorArea(int id) {
    	if (id != 0) {
			ViewGroup vp = (ViewGroup)getListView().getParent();
			LayoutInflater ltInflater = getLayoutInflater();
	        View view = ltInflater.inflate(id, null);
	        if (view != null) {
				try {
					vp.addView(view);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
    	}
    }
    
    public LayoutInflater getLayoutInflater() {
    	return getActivity().getLayoutInflater();
    }
	
	public ObjStore<T> getObjStore() {
		return objStore;
	}


	protected abstract ObjStore<T> makeObjStore();

	protected abstract AppListViewAdapter<T> makeAdapter(ObjStore<T> objStore);
	
	protected abstract void fillAdapterData(AppListViewAdapter<T> adapter);
	
//	protected void onResume() {
//		super.onResume();
//	}
//	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflateMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (getObjectActionList() != null) {
			for (ObjectAction<T> oa : getObjectActionList()) {
				if (oa.getActionId() == item.getItemId()) {
					if (oa.isMultiple()) {
						oa.doActionList(getListSelectedObjects());
					} else {
						oa.doAction(getListSelected());
					}
					return true;
				}
			}
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

    public AppListViewAdapter<T> getAdapter() {
		return adapter;
	}

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    }

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    	getListView().setItemChecked(position, true);
		return true;
	}
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
        {
            onListItemClick((ListView)parent, v, position, id);
        }
    };

    public View findViewById(int id) {
    	return getActivity().findViewById(id);
    }    
	//TODO orientation - save state
	// initialize on resume
    
}

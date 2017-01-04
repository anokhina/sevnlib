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
package ru.org.sevn.alib.dbgui;

import java.sql.SQLException;
import java.util.Arrays;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import ru.org.sevn.alib.data.ObjStore;
import ru.org.sevn.alib.db.AnyDatabaseHelper;
import ru.org.sevn.alib.gui.AppListViewAdapter;
import ru.org.sevn.alib.gui.AppSimpleCursorAdapter;
import ru.org.sevn.alib.gui.lform.ListFormFragment;
import ru.org.sevn.alib.gui.lform.action.ObjectAction;

public abstract class CursorListFormFragment<T> extends ListFormFragment<T> implements LoaderCallbacks<Cursor> {
	
    private AnyDatabaseHelper databaseHelper = null;
	public AnyDatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = makeDatabaseHelper();
		}
		return databaseHelper;
	}
	
	protected abstract Class<T> getObjectClass();
	protected abstract AnyDatabaseHelper makeDatabaseHelper();
	
	@Override
	protected ObjStore<T> makeObjStore() {
		ObjStore<T> ret;
		try {
			ret = getHelper().getObjStore(getObjectClass());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} 
		return ret;
	}

	@Override
	protected void fillAdapterData(AppListViewAdapter<T> adapter) {
	}

	public void onDestroy() {
		super.onDestroy();
		getLoaderManager().destroyLoader(LOADER_OBJECTS);
		if (databaseHelper != null) {
			databaseHelper.close();
			databaseHelper = null;
		}		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemLongClick(parent, view, position, id);
		ObjectAction<T> act = getLongClickAction();
		if (act != null) {
			if (act.isMultiple()) {
				act.doActionList(Arrays.asList(((AppSimpleCursorAdapter<T>)getListAdapter()).getObjectItem(position)));
			} else {
				act.doAction(((AppSimpleCursorAdapter<T>)getListAdapter()).getObjectItem(position));
			}
		}
		return true;
	}

	private ObjectAction<T> longClickAction;
	
	public void setLongClickAction(ObjectAction<T> longClickAction) {
		this.longClickAction = longClickAction;
	}

	protected ObjectAction<T> getLongClickAction() {
		return longClickAction;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyCursorLoader ret = new MyCursorLoader(this);
		((AppSimpleCursorAdapter)getListAdapter()).setLoader(ret);
		return ret;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		((AppSimpleCursorAdapter)getListAdapter()).changeCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
	
	protected abstract QueryFilter getFilterFromUI();
	
	protected void filterFromUI() {
        setQueryFilter(getFilterFromUI());
	}
	
	// TODO state
	private QueryFilter queryFilter;
	
	public QueryFilter getQueryFilter() {
		return queryFilter;
	}
	
	private void setQueryFilter(QueryFilter filter) {
		queryFilter = filter;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(LOADER_OBJECTS, null, this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		filterFromUI();
	}
	
	static class MyCursorLoader extends CursorLoader {

		private CursorListFormFragment activity;
		
		public MyCursorLoader(CursorListFormFragment context) {
			super(context.getActivity());
			this.activity = context;
		}

		@Override
		public Cursor loadInBackground() {
			return activity.getObjectCursor(getActivity().getHelper(), getActivity().getQueryFilter());
		}
		
		public CursorListFormFragment getActivity() {
			return activity;
		}
	}
	
	public static final int LOADER_OBJECTS = 1;
	
	public abstract Cursor getObjectCursor(AnyDatabaseHelper helper, QueryFilter queryFilter);
	
}

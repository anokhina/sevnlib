package ru.org.sevn.alib.gui;

import java.util.Collection;

import android.content.Context;
import android.database.Cursor;
import ru.org.sevn.alib.data.ObjStore;
import ru.org.sevn.alib.data.ObjStore.ObjStoreException;
import ru.org.sevn.alib.db.Cursor2Object;

public class StoreSimpleCursorAdapter<T> extends AppSimpleCursorAdapter<T> {

	private ObjStore<T> objStore;
	
	@Deprecated
	public StoreSimpleCursorAdapter(Context context, int layout_id, ObjStore<T> os, Cursor c, String[] columns, int[] ids, int flags) {
		this(context, layout_id, os, c, null, columns, ids, flags);
	}
	public StoreSimpleCursorAdapter(Context context, int layout_id, ObjStore<T> os, Cursor c, Cursor2Object<T> corsorConverter, String[] columns, int[] ids, int flags) {
		super(context, layout_id, c, corsorConverter, columns, ids, flags);
		this.objStore = os;
	}
	
	public ObjStore<T> getObjStore() {
		return objStore;
	}
	
	@Override
	public void add(Collection<T> obj) {
		try {
			for(T o : obj) {
				objStore.add(o);
			}
			super.add(obj);
		} catch (ObjStoreException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(Collection<T> obj) {
		try {
			for(T o : obj) {
				objStore.remove(o);
			}
			super.remove(obj);
		} catch (ObjStoreException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void update(Collection<T> lst) {
		try {
			if (lst != null) {
				for (T obj : lst) {
					objStore.update(obj);
				}
			}
			super.update(lst);
		} catch (ObjStoreException e) {
			throw new RuntimeException(e);
		}
	}
}

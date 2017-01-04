package ru.org.sevn.alib.gui;

import java.util.Collection;

import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import ru.org.sevn.alib.db.Cursor2Object;

public abstract class AppSimpleCursorAdapter<T> extends SimpleCursorAdapter implements AppListViewAdapter<T>{
	private Loader loader;
	private Cursor2Object<T> cursorConverter;
	
	public Cursor2Object<T> getCursorConverter() {
		return cursorConverter;
	}

	public void setCursorConverter(Cursor2Object<T> corsorConverter) {
		this.cursorConverter = corsorConverter;
	}

	@Deprecated
	public AppSimpleCursorAdapter(Context context, int layout_id, Cursor c, String[] columns, int[] ids, int flags) {
		this(context, layout_id, c, null, columns, ids, flags);
	}
	
	public AppSimpleCursorAdapter(Context context, int layout_id, Cursor c, Cursor2Object<T> corsorConverter, String[] columns, int[] ids, int flags) {
		super(context, layout_id, c, 
				columns, 
				ids, FLAG_REGISTER_CONTENT_OBSERVER);
		setCursorConverter(corsorConverter);
	}
	
    public T getObjectItem(int position) {
    	Cursor cursor = (Cursor)this.getItem(position); 
        return getObjectItem(cursor);
    }

	public T getObjectItem(Cursor cursor) {
    	Cursor2Object<T> cc = getCursorConverter();
    	if (cc != null) {
    		return cc.getObject(cursor);
    	}
		return null;
	}
	
	@Override
	public void addAll(Collection<? extends T> queryForAll) {
		update();
	}

	@Override
	public void add(Collection<T> obj) {
		update();
	}

	@Override
	public void remove(Collection<T> obj) {
		update();
	}
	
	@Override
	public void update(Collection<T> obj) {
		update();
	}
	
	private void update() {
		if (getLoader() != null) {
			getLoader().forceLoad();
		}
	}
	@Override
	public void updateList() {
		update();
	}
	public Loader getLoader() {
		return loader;
	}
	public void setLoader(Loader loader) {
		this.loader = loader;
	}
	
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    	if (!bindViewByObject(view, context, cursor)) {
    		super.bindView(view, context, cursor);
    	}
    }
    public boolean bindViewByObject(View view, Context context, Cursor cursor) {
    	return false;
    }
}
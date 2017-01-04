package ru.org.sevn.alib.db;

import android.database.Cursor;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import ru.org.sevn.alib.data.Object2String;

public class CursorMapper<T> implements CursorToStringConverter, Cursor2Object<T> {
	
	private final Object2String<T> object2StringConverter;
	private final Cursor2Object<T> cursor2ObjectConverter;
	
	public CursorMapper(Object2String<T> o2s, Cursor2Object<T> c2o) {
		this.object2StringConverter = o2s;
		this.cursor2ObjectConverter = c2o;
	}

	@Override
	public T getObject(Cursor arg0) {
		return cursor2ObjectConverter.getObject(arg0);
	}

	@Override
	public CharSequence convertToString(Cursor cursor) {
		return convertToStringObject(getObject(cursor));
	}

	public CharSequence convertToStringObject(T cur) {
		return object2StringConverter.getStringRepresentation(cur);
	}
}